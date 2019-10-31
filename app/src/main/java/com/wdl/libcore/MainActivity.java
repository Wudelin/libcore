package com.wdl.libcore;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.wdl.core.prompt.WToast;
import com.wdl.core.util.LanguageUtil;
import com.wdl.core.util.WActivityStack;
import com.wdl.core.util.WLogger;
import com.wdl.core.util.WResUtil;

import java.util.Locale;

public class MainActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //LanguageUtil.applyLang(Locale.ENGLISH);
        setContentView(R.layout.activity_main);
        WLogger.e("MainActivity");
        WToast.show("MainActivity");
        WLogger.e("MainActivity" + WActivityStack.getInstance().getSize());
        WLogger.e("MainActivity" + WResUtil.dp2px(20) + "  " + WResUtil.getScreenWidth() + " " + WResUtil.getScreenHeight());

        findViewById(R.id.btn_en).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                LanguageUtil.applyLang(MainActivity.this,Locale.ENGLISH);
            }
        });

        findViewById(R.id.btn_zh).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                LanguageUtil.applyLang(MainActivity.this,Locale.CHINESE);
            }
        });

    }
}
