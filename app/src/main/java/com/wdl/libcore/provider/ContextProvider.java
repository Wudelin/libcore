package com.wdl.libcore.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import androidx.annotation.Nullable;
import com.wdl.libcore.util.WLibrary;


public class ContextProvider extends ContentProvider
{
    @Override
    public boolean onCreate()
    {
        WLibrary.set(getContext());
        return false;
    }

    @Override
    public Cursor query(@Nullable Uri uri, String[] strings, String s, String[] strings1, String s1)
    {
        return null;
    }

    @Override
    public String getType(@Nullable Uri uri)
    {
        return null;
    }

    @Override
    public Uri insert(@Nullable Uri uri, ContentValues contentValues)
    {
        return null;
    }

    @Override
    public int delete(@Nullable Uri uri, String s, String[] strings)
    {
        return 0;
    }

    @Override
    public int update(@Nullable Uri uri, ContentValues contentValues, String s, String[] strings)
    {
        return 0;
    }
}
