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

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class FileManagerViewModelFactory extends ViewModelProvider.NewInstanceFactory
{
    private final Context context;
    private final FileManagerConfig config;
    private final String startDir;

    public FileManagerViewModelFactory(@NonNull Context context,
                                       FileManagerConfig config,
                                       String startDir)
    {
        this.context = context;
        this.config = config;
        this.startDir = startDir;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass)
    {
        if (modelClass.isAssignableFrom(FileManagerViewModel.class))
            return (T)new FileManagerViewModel(context, config, startDir);

        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
