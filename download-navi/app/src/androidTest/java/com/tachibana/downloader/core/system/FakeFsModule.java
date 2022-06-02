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

import android.net.Uri;

import androidx.annotation.NonNull;

import java.io.FileDescriptor;
import java.util.List;

public class FakeFsModule implements FsModule
{
    private List<String> existsFileNames;

    public FakeFsModule(List<String> existsFileNames)
    {
        this.existsFileNames = existsFileNames;
    }

    @Override
    public String getName(@NonNull Uri filePath)
    {
        String path = filePath.getPath();

        String fileName = path.substring(path.lastIndexOf("/") + 1);

        if (!existsFileNames.contains(fileName))
            return null;

        return fileName;
    }

    @Override
    public String getDirName(@NonNull Uri dir)
    {
        String path = dir.getPath();

        String dirName = path.substring(path.lastIndexOf("/") + 1);

        if (!existsFileNames.contains(dirName))
            return null;

        return dirName;
    }

    @Override
    public Uri getFileUri(@NonNull Uri dir, @NonNull String fileName, boolean create)
    {
        if (!create && !existsFileNames.contains(fileName))
            return null;

        return Uri.parse("file://" + dir.getPath() + "/" + fileName);
    }

    @Override
    public Uri getFileUri(@NonNull String relativePath, @NonNull Uri dir)
    {
        String fileName = relativePath.substring(relativePath.lastIndexOf("/") + 1);
        if (!existsFileNames.contains(fileName))
            return null;

        return Uri.parse("file://" + dir.getPath() + "/" + relativePath);
    }

    @Override
    public boolean delete(@NonNull Uri filePath)
    {
        return true;
    }

    @Override
    public boolean exists(@NonNull Uri filePath) {
        return true;
    }

    @Override
    public FileDescriptorWrapper openFD(@NonNull Uri path)
    {
        return new FakeFileDescriptorWrapper(FileDescriptor.out);
    }

    @Override
    public long getDirAvailableBytes(@NonNull Uri dir)
    {
        return -1;
    }

    @Override
    public long getFileSize(@NonNull Uri filePath) {
        return -1;
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
