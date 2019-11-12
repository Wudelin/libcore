package com.wdl.libcore;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipboardManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.wdl.core.executor.ExecutorManager;
import com.wdl.core.prompt.WToast;
import com.wdl.core.util.ClipBoardUtil;
import com.wdl.core.util.ColorUtil;
import com.wdl.core.util.FileProvider;
import com.wdl.core.util.IntentUtil;
import com.wdl.core.util.LanguageUtil;
import com.wdl.core.util.WActivityStack;
import com.wdl.core.util.WLogger;
import com.wdl.core.util.WResUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

public class MainActivity extends AppCompatActivity
{
    private ImageView iv;
    private EditText et;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //LanguageUtil.applyLang(Locale.ENGLISH);
        setContentView(R.layout.activity_main);
        /**
         * 限定参数
         */
        Test t = new Test();
        int x = t.getActivityInfo(Test.GET_META_DATA);
        /////////////////////////////////
        WLogger.e("MainActivity");
        WToast.show("MainActivity");
        WLogger.e("MainActivity" + WActivityStack.getInstance().getSize());
        WLogger.e("MainActivity" + WResUtil.dp2px(20) + "  " + WResUtil.getScreenWidth() + " " + WResUtil.getScreenHeight());
        iv = findViewById(R.id.iv);
        findViewById(R.id.btn_en).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                LanguageUtil.applyLang(MainActivity.this, Locale.ENGLISH);
            }
        });

        findViewById(R.id.btn_zh).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                LanguageUtil.applyLang(MainActivity.this, Locale.CHINESE);
            }
        });

        findViewById(R.id.btn_call).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(IntentUtil.getCallIntent("18806039939"));
            }
        });

        findViewById(R.id.btn_call_dial).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(IntentUtil.getDialIntent("18806039939"));
            }
        });

        findViewById(R.id.btn_bro).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (IntentUtil.isIntentAvailable(MainActivity.this, IntentUtil.getBroseIntent("http://www.baidu.com")))
                    startActivity(IntentUtil.getBroseIntent("http://www.baidu.com"));
            }
        });

        findViewById(R.id.btn_album).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivityForResult(IntentUtil.getSelectImageFromAlbumIntent(), 0x01);
            }
        });

        findViewById(R.id.btn_capture).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String filename = new SimpleDateFormat("yyyyMMdd-HHmmss", Locale.CHINA)
                        .format(new Date()) + ".png";
                file = new File(Environment.getExternalStorageDirectory(), filename);
                startActivityForResult(IntentUtil.getCaptureIntent(MainActivity.this, file), 0x02);
            }
        });

        et = findViewById(R.id.editText);
        et.setOnLongClickListener(new View.OnLongClickListener()
        {
            @Override
            public boolean onLongClick(View v)
            {
                ClipBoardUtil.setPlainText(et.getText());
                return false;
            }
        });

        textView = findViewById(R.id.textView);
        textView.setOnLongClickListener(new View.OnLongClickListener()
        {
            @Override
            public boolean onLongClick(View v)
            {
                textView.setText(ClipBoardUtil.getText());
                return false;
            }
        });

        ExecutorManager.getExecutorManager().execute(ExecutorManager.ExType.IO, new Runnable()
        {
            @Override
            public void run()
            {
                WLogger.e("ExecutorManager IO:" + Thread.currentThread() + "");
            }
        });

        ExecutorManager.getExecutorManager().execute(ExecutorManager.ExType.IO, new Runnable()
        {
            @Override
            public void run()
            {
                WLogger.e("ExecutorManager IO2:" + Thread.currentThread() + "");
            }
        });

        ExecutorManager.getExecutorManager().execute(ExecutorManager.ExType.IO, new Callable()
        {
            @Override
            public Object call() throws Exception
            {
                WLogger.e("ExecutorManager IO3: Callable" );
                return null;
            }
        });

        ExecutorManager.getExecutorManager().execute(ExecutorManager.ExType.NETWORK, new Runnable()
        {
            @Override
            public void run()
            {
                WLogger.e("ExecutorManager NETWORK:" + Thread.currentThread() + "");
            }
        });

        ExecutorManager.getExecutorManager().execute(ExecutorManager.ExType.OTHER, new Runnable()
        {
            @Override
            public void run()
            {
                WLogger.e("ExecutorManager OTHER:" + Thread.currentThread() + "");
            }
        });

    }

    private File file;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK)
        {
            if (requestCode == 0x01)
            {
                Uri uri = data.getData();
                if (uri != null)
                {
                    InputStream is = null;
                    try
                    {
                        is = getContentResolver().openInputStream(uri);
                        iv.setImageBitmap(BitmapFactory.decodeStream(is));
                    } catch (FileNotFoundException e)
                    {
                        e.printStackTrace();
                    } finally
                    {
                        if (is != null)
                        {
                            try
                            {
                                is.close();
                            } catch (IOException e)
                            {
                                e.printStackTrace();
                            }
                        }
                    }

                }
            } else if (requestCode == 0x02)
            {
                Uri uri = FileProvider.getUriForFile(this, file);
                if (uri != null)
                {
                    InputStream is = null;
                    try
                    {
                        is = getContentResolver().openInputStream(uri);
                        iv.setImageBitmap(BitmapFactory.decodeStream(is));
                    } catch (FileNotFoundException e)
                    {
                        e.printStackTrace();
                    } finally
                    {
                        if (is != null)
                        {
                            try
                            {
                                is.close();
                            } catch (IOException e)
                            {
                                e.printStackTrace();
                            }
                        }
                    }

                }
            }
        }
    }
}
