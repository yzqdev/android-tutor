/*
 * Copyright (C) 2019 Yaroslav Pronin <proninyaroslav@mail.ru>
 *
 * This file is part of LibreTorrent.
 *
 * LibreTorrent is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * LibreTorrent is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with LibreTorrent.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.tachibana.downloader.ui.filemanager;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;
import androidx.lifecycle.ViewModel;

import com.tachibana.downloader.core.system.FileSystemFacade;
import com.tachibana.downloader.core.system.SystemFacadeHelper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.reactivex.subjects.BehaviorSubject;

public class FileManagerViewModel extends ViewModel
{
    @SuppressWarnings("unused")
    private static final String TAG = FileManagerViewModel.class.getSimpleName();

    private final Context appContext;
    public String startDir;
    /* Current directory */
    public ObservableField<String> curDir = new ObservableField<>();
    public FileManagerConfig config;
    public BehaviorSubject<List<FileManagerNode>> childNodes = BehaviorSubject.create();
    public Exception errorReport;
    public FileSystemFacade fs;

    public FileManagerViewModel(@NonNull Context appContext, FileManagerConfig config, String startDir)
    {
        this.appContext = appContext;
        this.config = config;
        this.fs = SystemFacadeHelper.getFileSystemFacade(appContext);
        this.startDir = startDir;

        String path = config.path;
        if (TextUtils.isEmpty(path)) {
            if (startDir != null) {
                File dir = new File(startDir);
                boolean accessMode = config.showMode == FileManagerConfig.FILE_CHOOSER_MODE ?
                        dir.canRead() :
                        dir.canWrite();
                if (!(dir.exists() && accessMode))
                    startDir = fs.getDefaultDownloadPath();
            } else {
                startDir = fs.getDefaultDownloadPath();
            }

        } else {
            startDir = path;
        }

        try {
            if (startDir != null) {
                startDir = new File(startDir).getCanonicalPath();
            }
            updateCurDir(startDir);

        } catch (IOException e) {
            Log.e(TAG, Log.getStackTraceString(e));
        }
    }

    public void refreshCurDirectory()
    {
        childNodes.onNext(getChildItems());
    }

    private void updateCurDir(String newPath)
    {
        if (newPath == null)
            return;
        curDir.set(newPath);
        childNodes.onNext(getChildItems());
    }

    /*
     * Get subfolders or files.
     */

    private List<FileManagerNode> getChildItems()
    {
        List<FileManagerNode> items = new ArrayList<>();
        String dir = curDir.get();
        if (dir == null)
            return items;

        try {
            File dirFile = new File(dir);
            if (!(dirFile.exists() && dirFile.isDirectory()))
                return items;

            /* Adding parent dir for navigation */
            if (!dirFile.getPath().equals(FileManagerNode.ROOT_DIR))
                items.add(0, new FileManagerNode(FileManagerNode.PARENT_DIR, FileNode.Type.DIR, true));

            File[] files = dirFile.listFiles();
            if (files == null)
                return items;
            for (File file : filterDirectories(files)) {
                if (file.isDirectory())
                    items.add(new FileManagerNode(file.getName(), FileNode.Type.DIR, true));
                else
                    items.add(new FileManagerNode(file.getName(), FileManagerNode.Type.FILE,
                            config.showMode == FileManagerConfig.FILE_CHOOSER_MODE));
            }

        } catch (Exception e) {
            /* Ignore */
        }

        return items;
    }

    List<File> filterDirectories(File[] files) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R
                || config.showMode == FileManagerConfig.FILE_CHOOSER_MODE) {
            return Arrays.asList(files);
        }

        var filtered = new ArrayList<File>();
        for (var file : files) {
            if (file.isFile() || file.canWrite()) {
                filtered.add(file);
            }
        }

        return filtered;
    }

    public boolean createDirectory(String name)
    {
        if (TextUtils.isEmpty(name))
            return false;

        File newDir = new File(curDir.get(), name);

        return !newDir.exists() && newDir.mkdir();
    }

    public void openDirectory(String name) throws IOException, SecurityException
    {
        File dir = new File(curDir.get(), name);
        String path = dir.getCanonicalPath();

        if (!(dir.exists() && dir.isDirectory()))
            path = startDir;
        else if (!dir.canRead())
            throw new SecurityException("Permission denied");

        updateCurDir(path);
    }

    public void jumpToDirectory(String path) throws SecurityException
    {
        File dir = new File(path);

        if (!(dir.exists() && dir.isDirectory()))
            path = startDir;
        else if (!dir.canRead())
            throw new SecurityException("Permission denied");

        updateCurDir(path);
    }

    /*
     * Navigate back to an upper directory.
     */

    public void upToParentDirectory() throws SecurityException
    {
        String path = curDir.get();
        if (path == null)
            return;
        File dir = new File(path);

        File parentDir = dir.getParentFile();
        if (parentDir != null && !parentDir.canRead())
            throw new SecurityException("Permission denied");

        updateCurDir(dir.getParent());
    }

    public boolean fileExists(String fileName)
    {
        if (fileName == null)
            return false;

        fileName = fs.appendExtension(fileName, config.mimeType);

        return new File(curDir.get(), fileName).exists();
    }

    public Uri createFile(String fileName) throws SecurityException
    {
        if (TextUtils.isEmpty(fileName))
            fileName = config.fileName;

        fileName = fs.appendExtension(fs.buildValidFatFilename(fileName), config.mimeType);

        File f = new File(curDir.get(), fileName);
        if (!f.getParentFile().canWrite())
            throw new SecurityException("Permission denied");
        try {
            if (f.exists() && !f.delete())
                return null;
            if (!f.createNewFile())
                return null;

        } catch (IOException e) {
            return null;
        }

        return Uri.fromFile(f);
    }

    public Uri getCurDirectoryUri() throws SecurityException
    {
        String path = curDir.get();
        if (path == null)
            return null;

        File dir = new File(path);
        if (!(dir.canWrite() && dir.canRead()))
            throw new SecurityException("Permission denied");

        return Uri.fromFile(dir);
    }

    public Uri getFileUri(String fileName) throws SecurityException
    {
        String path = curDir.get();
        if (path == null)
            return null;

        File f = new File(path, fileName);
        if (!f.canRead())
            throw new SecurityException("Permission denied");

        return Uri.fromFile(f);
    }

    public void takeSafPermissions(Intent data)
    {
        ContentResolver resolver = appContext.getContentResolver();

        var takeFlags = data.getFlags();
        takeFlags &= (Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

        Uri uri = data.getData();
        if (uri != null)
            resolver.takePersistableUriPermission(uri, takeFlags);
    }
}
