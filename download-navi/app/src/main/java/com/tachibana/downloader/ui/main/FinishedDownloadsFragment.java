/*
 * Copyright (C) 2019 Tachibana General Laboratories, LLC
 * Copyright (C) 2019 Yaroslav Pronin <proninyaroslav@mail.ru>
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

package com.tachibana.downloader.ui.main;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.tachibana.downloader.R;
import com.tachibana.downloader.core.model.data.StatusCode;
import com.tachibana.downloader.core.model.data.entity.DownloadInfo;
import com.tachibana.downloader.core.utils.Utils;
import com.tachibana.downloader.ui.BaseAlertDialog;
import com.tachibana.downloader.ui.adddownload.AddDownloadActivity;
import com.tachibana.downloader.ui.adddownload.AddInitParams;

import java.util.Collections;

import io.reactivex.disposables.Disposable;

public class FinishedDownloadsFragment extends DownloadsFragment
    implements DownloadListAdapter.FinishClickListener,
               DownloadListAdapter.ErrorClickListener
{
    @SuppressWarnings("unused")
    private static final String TAG = FinishedDownloadsFragment.class.getSimpleName();

    private static final String TAG_DELETE_DOWNLOAD_DIALOG = "delete_download_dialog";
    private static final String TAG_DOWNLOAD_FOR_DELETION = "download_for_deletion";

    private BaseAlertDialog deleteDownloadDialog;
    private BaseAlertDialog.SharedViewModel dialogViewModel;
    private DownloadInfo downloadForDeletion;

    public static FinishedDownloadsFragment newInstance()
    {
        FinishedDownloadsFragment fragment = new FinishedDownloadsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);

        return fragment;
    }

    public FinishedDownloadsFragment()
    {
        super((item) -> StatusCode.isStatusCompleted(item.info.statusCode));
    }

    @Override
    public void onStart()
    {
        super.onStart();

        subscribeAdapter();
        subscribeAlertDialog();
    }

    private void subscribeAlertDialog()
    {
        Disposable d = dialogViewModel.observeEvents()
                .subscribe((event) -> {
                    if (event.dialogTag == null || !event.dialogTag.equals(TAG_DELETE_DOWNLOAD_DIALOG) || deleteDownloadDialog == null)
                        return;
                    switch (event.type) {
                        case POSITIVE_BUTTON_CLICKED:
                            Dialog dialog = deleteDownloadDialog.getDialog();
                            if (dialog != null && downloadForDeletion != null) {
                                CheckBox withFile = dialog.findViewById(R.id.delete_with_file);
                                deleteDownload(downloadForDeletion, withFile.isChecked());
                            }
                        case NEGATIVE_BUTTON_CLICKED:
                            downloadForDeletion = null;
                            deleteDownloadDialog.dismiss();
                            break;
                    }
                });
        disposables.add(d);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null)
            downloadForDeletion = savedInstanceState.getParcelable(TAG_DOWNLOAD_FOR_DELETION);

        FragmentManager fm = getChildFragmentManager();
        deleteDownloadDialog = (BaseAlertDialog)fm.findFragmentByTag(TAG_DELETE_DOWNLOAD_DIALOG);
        dialogViewModel = new ViewModelProvider(activity).get(BaseAlertDialog.SharedViewModel.class);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState)
    {
        outState.putParcelable(TAG_DOWNLOAD_FOR_DELETION, downloadForDeletion);

        super.onSaveInstanceState(outState);
    }

    @Override
    public void onItemClicked(@NonNull DownloadItem item) {
        Intent file = Utils.createOpenFileIntent(activity.getApplicationContext(), item.info);
        if (file != null) {
            startActivity(Intent.createChooser(file, getString(R.string.open_using)));
        } else {
            Toast.makeText(activity.getApplicationContext(), getString(R.string.file_not_available), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onItemMenuClicked(int menuId, @NonNull DownloadItem item)
    {
        switch (menuId) {
            case R.id.delete_menu:
                downloadForDeletion = item.info;
                showDeleteDownloadDialog();
                break;
            case R.id.open_details_menu:
                showDetailsDialog(item.info.id);
                break;
            case R.id.share_menu:
                shareDownload(item);
                break;
            case R.id.share_url_menu:
                shareUrl(item);
                break;
            case R.id.redownload_menu:
                showAddDownloadDialog(item.info);
                break;
        }
    }

    @Override
    public void onItemResumeClicked(@NonNull DownloadItem item)
    {
        viewModel.resumeIfError(item.info);
    }

    private void showDeleteDownloadDialog()
    {
        if (!isAdded())
            return;

        FragmentManager fm = getChildFragmentManager();
        if (fm.findFragmentByTag(TAG_DELETE_DOWNLOAD_DIALOG) == null) {
            deleteDownloadDialog = BaseAlertDialog.newInstance(
                    getString(R.string.deleting),
                    getString(R.string.delete_selected_download),
                    R.layout.dialog_delete_downloads,
                    getString(R.string.ok),
                    getString(R.string.cancel),
                    null,
                    false);

            deleteDownloadDialog.show(fm, TAG_DELETE_DOWNLOAD_DIALOG);
        }
    }

    private void deleteDownload(DownloadInfo info, boolean withFile)
    {
        viewModel.deleteDownload(info, withFile);
    }

    private void shareDownload(DownloadItem item) {
        Intent intent = Utils.makeFileShareIntent(activity.getApplicationContext(), Collections.singletonList(item));
        if (intent != null) {
            startActivity(Intent.createChooser(intent, getString(R.string.share_via)));
        } else {
            Toast.makeText(activity.getApplicationContext(),
                    getResources().getQuantityString(R.plurals.unable_sharing, 1), Toast.LENGTH_SHORT).show();
        }
    }

    private void shareUrl(DownloadItem item)
    {
        startActivity(Intent.createChooser(
                Utils.makeShareUrlIntent(item.info.url),
                getString(R.string.share_via)));
    }

    private void showAddDownloadDialog(DownloadInfo info)
    {
        AddInitParams initParams = new AddInitParams();
        initParams.url = info.url;
        initParams.dirPath = info.dirPath;
        initParams.fileName = info.fileName;
        initParams.description = info.description;
        initParams.userAgent = info.userAgent;
        initParams.unmeteredConnectionsOnly = info.unmeteredConnectionsOnly;
        initParams.retry = info.retry;
        initParams.replaceFile = true;

        Intent i = new Intent(activity, AddDownloadActivity.class);
        i.putExtra(AddDownloadActivity.TAG_INIT_PARAMS, initParams);
        startActivity(i);
    }
}
