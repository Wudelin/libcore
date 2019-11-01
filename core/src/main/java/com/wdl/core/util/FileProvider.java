package com.wdl.core.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;

import java.io.File;

/**
 * Create by: wdl at 2019/11/1 10:24
 * 适配Android 7.0
 */
@SuppressWarnings("unused")
public final class FileProvider
{

    private FileProvider()
    {

    }

    public static Uri getUriForFile(Context context, File file)
    {
        Uri fileUri;
        if (Build.VERSION.SDK_INT >= 24)
        {
            fileUri = getUriForFile24(context, file);
        } else
        {
            fileUri = Uri.fromFile(file);
        }
        return fileUri;
    }

    public static Uri getUriForFile24(Context context, File file)
    {
        return androidx.core.content.FileProvider.getUriForFile(context,
                context.getPackageName() + ".android7.fileprovider",
                file);
    }


    public static void setIntentDataAndType(Context context,
                                            Intent intent,
                                            String type,
                                            File file,
                                            boolean writeAble)
    {
        if (Build.VERSION.SDK_INT >= 24)
        {
            intent.setDataAndType(getUriForFile(context, file), type);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            if (writeAble)
            {
                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            }
        } else
        {
            intent.setDataAndType(Uri.fromFile(file), type);
        }
    }
}

