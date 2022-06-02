/*
 * Copyright (C) 2018-2022 Tachibana General Laboratories, LLC
 * Copyright (C) 2018-2022 Yaroslav Pronin <proninyaroslav@mail.ru>
 *
 * This file is part of Download Navi.
 *
 * Download Navi is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Download Navi is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Download Navi.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.tachibana.downloader.core.model;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.tachibana.downloader.R;
import com.tachibana.downloader.core.RepositoryHelper;
import com.tachibana.downloader.core.exception.FileAlreadyExistsException;
import com.tachibana.downloader.core.model.data.DownloadResult;
import com.tachibana.downloader.core.model.data.StatusCode;
import com.tachibana.downloader.core.model.data.entity.DownloadInfo;
import com.tachibana.downloader.core.settings.SettingsRepository;
import com.tachibana.downloader.core.storage.DataRepository;
import com.tachibana.downloader.core.system.FileDescriptorWrapper;
import com.tachibana.downloader.core.system.FileSystemFacade;
import com.tachibana.downloader.core.system.SystemFacade;
import com.tachibana.downloader.core.system.SystemFacadeHelper;
import com.tachibana.downloader.core.utils.DigestUtils;
import com.tachibana.downloader.core.utils.Utils;
import com.tachibana.downloader.receiver.ConnectionReceiver;
import com.tachibana.downloader.receiver.PowerReceiver;
import com.tachibana.downloader.service.DeleteDownloadsWorker;
import com.tachibana.downloader.service.DownloadService;

import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class DownloadEngine {
    @SuppressWarnings("unused")
    private static final String TAG = DownloadEngine.class.getSimpleName();

    private final Context appContext;
    private final DataRepository repo;
    private final SettingsRepository pref;
    private final FileSystemFacade fs;
    private final CompositeDisposable disposables = new CompositeDisposable();
    private final HashMap<UUID, DownloadThread> activeDownloads = new HashMap<>();
    private final ConcurrentLinkedQueue<DownloadEngineListener> listeners = new ConcurrentLinkedQueue<>();
    private final HashMap<UUID, ChangeableParams> duringChange = new HashMap<>();
    private final DownloadQueue queue = new DownloadQueue();

    private final PowerReceiver powerReceiver = new PowerReceiver();
    private final ConnectionReceiver connectionReceiver = new ConnectionReceiver();

    private static volatile DownloadEngine INSTANCE;

    public static DownloadEngine getInstance(@NonNull Context appContext) {
        if (INSTANCE == null) {
            synchronized (DownloadEngine.class) {
                if (INSTANCE == null)
                    INSTANCE = new DownloadEngine(appContext);
            }
        }

        return INSTANCE;
    }

    private DownloadEngine(Context appContext) {
        this.appContext = appContext;
        repo = RepositoryHelper.getDataRepository(appContext);
        pref = RepositoryHelper.getSettingsRepository(appContext);
        fs = SystemFacadeHelper.getFileSystemFacade(appContext);

        switchConnectionReceiver();
        switchPowerReceiver();

        disposables.add(pref.observeSettingsChanged()
                .subscribe(this::handleSettingsChanged));
    }

    public void addListener(DownloadEngineListener listener) {
        listeners.add(listener);
    }

    public void removeListener(DownloadEngineListener listener) {
        listeners.remove(listener);
    }

    public void runDownload(@NonNull DownloadInfo info) {
        DownloadScheduler.run(appContext, info);
    }

    public void runDownload(@NonNull UUID id) {
        DownloadScheduler.run(appContext, id);
    }

    public void reschedulePendingDownloads() {
        DownloadScheduler.rescheduleAll(appContext);
    }

    /*
     * Exclude pending downloads
     */

    public void rescheduleDownloads() {
        if (checkStopDownloads())
            stopDownloads();
        else
            resumeDownloads(true);
    }

    public void pauseResumeDownload(@NonNull UUID id) {
        disposables.add(repo.getInfoByIdSingle(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .filter((info) -> info != null)
                .subscribe((info) -> {
                            if (StatusCode.isStatusStoppedOrPaused(info.statusCode)) {
                                runDownload(info);
                            } else {
                                DownloadThread task = activeDownloads.get(id);
                                if (task != null && !duringChange.containsKey(id))
                                    task.requestPause();
                            }
                        },
                        (Throwable t) -> {
                            Log.e(TAG, "Getting info " + id + " error: " +
                                    Log.getStackTraceString(t));
                            if (checkNoDownloads())
                                notifyListeners(DownloadEngineListener::onDownloadsCompleted);
                        })
        );
    }

    public void resumeIfError(@NonNull UUID id) {
        disposables.add(repo.getInfoByIdSingle(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .filter((info) -> info != null)
                .subscribe((info) -> {
                            if (StatusCode.isStatusError(info.statusCode))
                                runDownload(info);
                        },
                        (Throwable t) -> {
                            Log.e(TAG, "Getting info " + id + " error: " +
                                    Log.getStackTraceString(t));
                            if (checkNoDownloads())
                                notifyListeners(DownloadEngineListener::onDownloadsCompleted);
                        })
        );
    }

    public synchronized void pauseAllDownloads() {
        for (Map.Entry<UUID, DownloadThread> entry : activeDownloads.entrySet()) {
            if (duringChange.containsKey(entry.getKey()))
                continue;
            DownloadThread task = entry.getValue();
            if (task == null)
                continue;
            task.requestPause();
        }
    }

    public void resumeDownloads(boolean ignorePaused) {
        DownloadScheduler.runAll(appContext, ignorePaused);
    }

    public void restoreDownloads() {
        DownloadScheduler.restoreDownloads(appContext);
    }

    public synchronized void stopDownloads() {
        for (Map.Entry<UUID, DownloadThread> entry : activeDownloads.entrySet()) {
            if (duringChange.containsKey(entry.getKey()))
                continue;
            DownloadThread task = entry.getValue();
            if (task != null)
                task.requestStop();
        }
    }

    public void deleteDownloads(boolean withFile, @NonNull UUID... idList) {
        String[] strIdList = new String[idList.length];
        for (int i = 0; i < idList.length; i++) {
            if (idList[i] != null)
                strIdList[i] = idList[i].toString();
        }

        runDeleteDownloadsWorker(strIdList, withFile);
    }

    public void deleteDownloads(boolean withFile, @NonNull DownloadInfo... infoList) {
        String[] strIdList = new String[infoList.length];
        for (int i = 0; i < infoList.length; i++) {
            if (infoList[i] != null)
                strIdList[i] = infoList[i].id.toString();
        }

        runDeleteDownloadsWorker(strIdList, withFile);
    }

    public boolean hasActiveDownloads() {
        return !activeDownloads.isEmpty();
    }

    public void changeParams(@NonNull UUID id,
                             @NonNull ChangeableParams params) {
        Intent i = new Intent(appContext, DownloadService.class);
        i.setAction(DownloadService.ACTION_CHANGE_PARAMS);
        i.putExtra(DownloadService.TAG_DOWNLOAD_ID, id);
        i.putExtra(DownloadService.TAG_PARAMS, params);

        appContext.startService(i);
    }

    private void verifyChecksum(@NonNull UUID id) {
        DownloadInfo info;
        try {
            info = repo.getInfoById(id);
        } catch (Exception e) {
            Log.e(TAG, "Getting info " + id + " error: " + Log.getStackTraceString(e));
            return;
        }
        if (doVerifyChecksum(info)) {
            info.statusCode = StatusCode.STATUS_SUCCESS;
            info.statusMsg = null;
        } else {
            info.statusCode = StatusCode.STATUS_CHECKSUM_ERROR;
            info.statusMsg = appContext.getString(R.string.error_verify_checksum);
        }
        repo.updateInfo(info, false, false);
    }

    private boolean doVerifyChecksum(DownloadInfo info) {
        if (TextUtils.isEmpty(info.checksum)) {
            return true;
        }
        String hash;
        try {
            if (DigestUtils.isMd5Hash(info.checksum)) {
                hash = calcHashSum(info, false);

            } else if (DigestUtils.isSha256Hash(info.checksum)) {
                hash = calcHashSum(info, true);

            } else {
                throw new IllegalArgumentException("Unknown checksum type:" + info.checksum);
            }
        } catch (IOException e) {
            return false;
        }
        return (hash != null && hash.equalsIgnoreCase(info.checksum));
    }

    private String calcHashSum(DownloadInfo info, boolean sha256Hash) throws IOException {
        Uri filePath = fs.getFileUri(info.dirPath, info.fileName);
        if (filePath == null)
            return null;

        try (FileDescriptorWrapper w = fs.getFD(filePath)) {
            FileDescriptor outFd = w.open("r");
            try (FileInputStream is = new FileInputStream(outFd)) {
                return (sha256Hash ? DigestUtils.makeSha256Hash(is) : DigestUtils.makeMd5Hash(is));
            }
        }
    }

    /*
     * Do not call directly
     */

    public synchronized void doRunDownload(@NonNull UUID id) {
        if (duringChange.containsKey(id))
            return;

        if (isMaxActiveDownloads()) {
            queue.push(id);
            return;
        }

        DownloadThread task = activeDownloads.get(id);
        if (task != null && task.isRunning())
            return;

        task = new DownloadThreadImpl(id, repo, pref, fs,
                SystemFacadeHelper.getSystemFacade(appContext));
        activeDownloads.put(id, task);
        disposables.add(Observable.fromCallable(task)
                .subscribeOn(Schedulers.io())
                .filter((result) -> result != null)
                .doOnNext(this::onBeforeDownloadCompleted)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onDownloadCompleted,
                        (Throwable t) -> {
                            Log.e(TAG, Log.getStackTraceString(t));
                            if (checkNoDownloads())
                                notifyListeners(DownloadEngineListener::onDownloadsCompleted);
                        }
                )
        );
    }

    /*
     * Do not call directly
     */

    public synchronized void doDeleteDownload(@NonNull DownloadInfo info, boolean withFile) {
        if (duringChange.containsKey(info.id))
            return;

        DownloadScheduler.undone(appContext, info);
        repo.deleteInfo(info, withFile);

        DownloadThread task = activeDownloads.get(info.id);
        if (task != null)
            task.requestStop();
        else if (checkNoDownloads())
            notifyListeners(DownloadEngineListener::onDownloadsCompleted);
    }

    private void runDeleteDownloadsWorker(String[] idList, boolean withFile) {
        Data data = new Data.Builder()
                .putStringArray(DeleteDownloadsWorker.TAG_ID_LIST, idList)
                .putBoolean(DeleteDownloadsWorker.TAG_WITH_FILE, withFile)
                .build();
        OneTimeWorkRequest work = new OneTimeWorkRequest.Builder(DeleteDownloadsWorker.class)
                .setInputData(data)
                .build();
        WorkManager.getInstance(appContext).enqueue(work);
    }

    /*
     * Do not call directly
     */

    public synchronized void doChangeParams(@NonNull UUID id,
                                            @NonNull ChangeableParams params) {
        if (duringChange.containsKey(id))
            return;

        duringChange.put(id, params);
        notifyListeners((listener) -> listener.onApplyingParams(id));

        DownloadThread task = activeDownloads.get(id);
        if (task != null && task.isRunning())
            task.requestStop();
        else
            applyParams(id, params, false);
    }

    private void applyParams(UUID id, ChangeableParams params, boolean runAfter) {
        disposables.add(repo.getInfoByIdSingle(id)
                .subscribeOn(Schedulers.io())
                .subscribe((info) -> {
                            Throwable[] err = new Throwable[1];
                            boolean urlChanged = false;
                            try {
                                if (info == null)
                                    throw new NullPointerException();
                                urlChanged = doApplyParams(info, params);

                            } catch (Throwable e) {
                                err[0] = e;
                            } finally {
                                duringChange.remove(id);
                                String name = (info == null ? null : info.fileName);
                                notifyListeners((listener) -> listener.onParamsApplied(id, name, err[0]));
                                if (runAfter || urlChanged)
                                    runDownload(id);
                            }
                        },
                        (Throwable t) -> {
                            Log.e(TAG, "Getting info " + id + " error: " +
                                    Log.getStackTraceString(t));
                            duringChange.remove(id);
                            notifyListeners((listener) -> listener.onParamsApplied(id, null, t));
                        }
                )
        );
    }

    private boolean doApplyParams(DownloadInfo info, ChangeableParams params) throws IOException, FileAlreadyExistsException {
        boolean changed = false;
        if (params.url != null) {
            changed = true;
            info.url = params.url;
        }
        if (params.description != null) {
            changed = true;
            info.description = params.description;
        }
        if (params.unmeteredConnectionsOnly != null) {
            changed = true;
            info.unmeteredConnectionsOnly = params.unmeteredConnectionsOnly;
        }
        if (params.retry != null) {
            changed = true;
            info.retry = params.retry;
        }
        if (params.checksum != null) {
            changed = true;
            info.checksum = params.checksum;
        }

        boolean nameChanged = params.fileName != null;
        boolean dirChanged = params.dirPath != null;
        boolean urlChanged = params.url != null;
        boolean checksumChanged = params.checksum != null;
        if (checksumChanged) {
            if (doVerifyChecksum(info)) {
                info.statusCode = StatusCode.STATUS_SUCCESS;
                info.statusMsg = null;
            } else {
                info.statusCode = StatusCode.STATUS_CHECKSUM_ERROR;
                info.statusMsg = appContext.getString(R.string.error_verify_checksum);
            }
        }
        if (nameChanged || dirChanged) {
            changed = true;
            fs.moveFile(info.dirPath, info.fileName,
                    (dirChanged ? params.dirPath : info.dirPath),
                    (nameChanged ? params.fileName : info.fileName),
                    true);
            if (nameChanged)
                info.fileName = params.fileName;
            if (dirChanged)
                info.dirPath = params.dirPath;
        }

        if (changed)
            repo.updateInfo(info, true, false);

        return urlChanged;
    }

    private interface CallListener {
        void apply(DownloadEngineListener listener);
    }

    private void notifyListeners(@NonNull CallListener l) {
        for (DownloadEngineListener listener : listeners) {
            if (listener != null)
                l.apply(listener);
        }
    }

    private boolean checkNoDownloads() {
        return activeDownloads.isEmpty();
    }

    private void onBeforeDownloadCompleted(DownloadResult result) throws IOException, FileAlreadyExistsException {
        if (result.status == DownloadResult.Status.FINISHED) {
            onFinished(result.infoId);
        }
    }

    private void onDownloadCompleted(DownloadResult result) {
        activeDownloads.remove(result.infoId);
        scheduleWaitingDownload();

        var id = result.infoId;
        var params = duringChange.get(id);
        if (params == null) {
            if (checkNoDownloads())
                notifyListeners(DownloadEngineListener::onDownloadsCompleted);
        } else {
            applyParams(id, params, true);
        }
    }

    private void onFinished(UUID id) throws IOException, FileAlreadyExistsException {
        DownloadInfo info;
        try {
            info = repo.getInfoById(id);
        } catch (Exception e) {
            Log.e(TAG, "Getting info " + id + " error: " + Log.getStackTraceString(e));
            return;
        }
        switch (info.statusCode) {
            case StatusCode.STATUS_SUCCESS:
                if (!TextUtils.isEmpty(info.checksum)) {
                    verifyChecksum(id);
                }
                checkMoveAfterDownload(info);
                break;
            case StatusCode.STATUS_WAITING_TO_RETRY:
            case StatusCode.STATUS_WAITING_FOR_NETWORK:
                runDownload(info);
                break;
            case HttpURLConnection.HTTP_UNAUTHORIZED:
                /* TODO: request authorization from user */
                break;
            case HttpURLConnection.HTTP_PROXY_AUTH:
                /* TODO: proxy support */
                break;
        }
    }

    private void checkMoveAfterDownload(DownloadInfo info) throws IOException, FileAlreadyExistsException {
        if (!pref.moveAfterDownload()) {
            return;
        }
        var movePath = Uri.parse(pref.moveAfterDownloadIn());
        if (movePath == null) {
            return;
        }
        fs.moveFile(info.dirPath, info.fileName, movePath, info.fileName, true);
        info.dirPath = movePath;
        repo.updateInfo(info, true, false);
    }

    private boolean isMaxActiveDownloads() {
        return activeDownloads.size() == pref.maxActiveDownloads();
    }

    private void scheduleWaitingDownload() {
        if (isMaxActiveDownloads())
            return;

        UUID id = queue.pop();
        if (id == null)
            return;

        runDownload(id);
    }

    private void handleSettingsChanged(String key) {
        boolean reschedule = false;

        if (key.equals(appContext.getString(R.string.pref_key_umnetered_connections_only)) ||
                key.equals(appContext.getString(R.string.pref_key_enable_roaming))) {
            reschedule = true;
            switchConnectionReceiver();

        } else if (key.equals(appContext.getString(R.string.pref_key_download_only_when_charging)) ||
                key.equals(appContext.getString(R.string.pref_key_battery_control))) {
            reschedule = true;
            switchPowerReceiver();

        } else if (key.equals(appContext.getString(R.string.pref_key_custom_battery_control))) {
            switchPowerReceiver();
        }

        if (reschedule) {
            reschedulePendingDownloads();
            rescheduleDownloads();
        }
    }

    private void switchPowerReceiver() {
        boolean batteryControl = pref.batteryControl();
        boolean customBatteryControl = pref.customBatteryControl();
        boolean onlyCharging = pref.onlyCharging();

        try {
            appContext.unregisterReceiver(powerReceiver);

        } catch (IllegalArgumentException e) {
            /* Ignore non-registered receiver */
        }
        if (customBatteryControl) {
            appContext.registerReceiver(powerReceiver, PowerReceiver.getCustomFilter());
            /* Custom receiver doesn't send sticky intent, reschedule manually */
            rescheduleDownloads();
        } else if (batteryControl || onlyCharging) {
            appContext.registerReceiver(powerReceiver, PowerReceiver.getFilter());
        }
    }

    private void switchConnectionReceiver() {
        boolean unmeteredOnly = pref.unmeteredConnectionsOnly();
        boolean roaming = pref.enableRoaming();

        try {
            appContext.unregisterReceiver(connectionReceiver);

        } catch (IllegalArgumentException e) {
            /* Ignore non-registered receiver */
        }
        if (unmeteredOnly || roaming)
            appContext.registerReceiver(connectionReceiver, ConnectionReceiver.getFilter());
    }

    private boolean checkStopDownloads() {
        boolean batteryControl = pref.batteryControl();
        boolean customBatteryControl = pref.customBatteryControl();
        int customBatteryControlValue = pref.customBatteryControlValue();
        boolean onlyCharging = pref.onlyCharging();
        boolean unmeteredOnly = pref.unmeteredConnectionsOnly();
        boolean roaming = pref.enableRoaming();

        SystemFacade systemFacade = SystemFacadeHelper.getSystemFacade(appContext);

        boolean stop = false;
        if (roaming)
            stop = Utils.isRoaming(systemFacade);
        if (unmeteredOnly)
            stop = Utils.isMetered(systemFacade);
        if (onlyCharging)
            stop |= !Utils.isBatteryCharging(appContext);
        if (customBatteryControl)
            stop |= Utils.isBatteryBelowThreshold(appContext, customBatteryControlValue);
        else if (batteryControl)
            stop |= Utils.isBatteryLow(appContext);

        return stop;
    }
}
