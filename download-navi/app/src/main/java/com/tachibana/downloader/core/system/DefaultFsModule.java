/*
 * Copyright (C) 2019-2021 Tachibana General Laboratories, LLC
 * Copyright (C) 2019-2021 Yaroslav Pronin <proninyaroslav@mail.ru>
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

package com.tachibana.downloader.core.system;

import android.content.Context;
import android.net.Uri;
import android.os.StatFs;

import androidx.annotation.NonNull;

import java.io.File;
import java.io.IOException;

class DefaultFsModule implements FsModule
{
    private final Context appContext;

    public DefaultFsModule(@NonNull Context appContext)
    {
        this.appContext = appContext;
    }

    @Override
    public String getName(@NonNull Uri filePath)
    {
        return new File(filePath.getPath()).getName();
    }

    @Override
    public String getDirName(@NonNull Uri dir)
    {
        return dir.getPath();
    }

    @Override
    public Uri getFileUri(@NonNull Uri dir, @NonNull String fileName, boolean create) throws IOException
    {
        File f = new File(dir.getPath(), fileName);
        if (create)
            f.createNewFile();

        return (f.exists() ? Uri.fromFile(f) : null);
    }

    @Override
    public Uri getFileUri(@NonNull String relativePath, @NonNull Uri dir)
    {
        if (!relativePath.startsWith(File.separator))
            relativePath = File.separator + relativePath;
        File f = new File(dir.getPath() + relativePath);

        return (f.exists() ? Uri.fromFile(f) : null);
    }

    @Override
    public boolean delete(@NonNull Uri filePath)
    {
        return new File(filePath.getPath()).delete();
    }

    @Override
    public boolean exists(@NonNull Uri filePath) {
        return new File(filePath.getPath()).exists();
    }

    @Override
    public FileDescriptorWrapper openFD(@NonNull Uri path)
    {
        return new FileDescriptorWrapperImpl(appContext, path);
    }

    @Override
    public long getDirAvailableBytes(@NonNull Uri dir)
    {
        long availableBytes;

        try {
            File file = new File(dir.getPath());
            availableBytes = file.getUsableSpace();

        } catch (Exception e) {
            /* This provides invalid space on some devices */
            StatFs stat = new StatFs(dir.getPath());
            availableBytes = stat.getAvailableBytes();
        }

        return availableBytes;
    }

    @Override
    public long getFileSize(@NonNull Uri filePath) {
        return new File(filePath.getPath()).length();
    }

    @Override
    public void takePermissions(@NonNull Uri path) {
        // None
    }

    @Override
    public String getDirPath(@NonNull Uri dir) {
        return dir.getPath();
    }
}
