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

package com.tachibana.downloader.core.system;

import android.content.Context;
import android.net.Uri;

import androidx.annotation.NonNull;

import com.tachibana.downloader.core.utils.Utils;

class FsModuleResolverImpl implements FsModuleResolver
{
    private final Context appContext;
    private final SafFsModule safModule;
    private final DefaultFsModule defaultModule;

    public FsModuleResolverImpl(@NonNull Context appContext)
    {
        this.appContext = appContext;
        this.safModule = new SafFsModule(appContext);
        this.defaultModule = new DefaultFsModule(appContext);
    }

    @Override
    public FsModule resolveFsByUri(@NonNull Uri uri)
    {
        if (Utils.isSafPath(appContext, uri))
            return safModule;
        else if (Utils.isFileSystemPath(uri))
            return defaultModule;
        else
            throw new IllegalArgumentException("Cannot resolve file system for the given uri: " + uri);
    }
}
