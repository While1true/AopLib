package com.aoplibxx;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.Spanned;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.aoplib.CancelThread;
import com.aoplib.Debounce;
import com.aoplib.NewThread;
import com.aoplib.UI;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Thread extends AppCompatActivity {

    private long aLong;
    private TextView viewById;
    private boolean start;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewById = findViewById(R.id.bt);

        viewById.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showxc("正在获取...");
                if (!start) {
                    start = true;
                    toast();
                }
            }
        });
        findViewById(R.id.stop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
                start=false;
            }
        });
        main();
        viewById.setText("点击请求网络吧,延迟3秒显示结果\n @UI @NewThread都可以设置延迟时间");
        System.out.println("MainActivity2主线程 " + java.lang.Thread.currentThread().getId());
    }

    @CancelThread("子线程")
    private void cancel() {
    }

    @NewThread(value = "子线程", repate = true, period = 10000)
    @Debounce(value = 2000)
    public void toast() {
        showxc("正在获取...");
        System.out.println("------------------start------------------------toast");
        System.out.println("Thread子线程 " + java.lang.Thread.currentThread().getId());
        try {
            URL url = new URL("https://read.qidian.com/chapter/6wiYP-yFPlNrZK4x-CuJuw2/lLgJiXhXccDgn4SMoDUcDQ2");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setInstanceFollowRedirects(true);
            if (connection.getResponseCode() == 200) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuffer stringBuffer = new StringBuffer();
                String str;
                while ((str = reader.readLine()) != null) {
                    stringBuffer.append(str);
                }
                viewById.getLayoutParams();
                showxc("开始解析...");
                Spanned text = Html.fromHtml(stringBuffer.toString());
                showxc("完成解析，准备延迟3s展示");
                show(text);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("-----------------end-------------------------toast");
    }

    @UI(delay = 3000, value = "主线程")
    private void show(CharSequence s) {
        TextView tv = findViewById(R.id.content);
        tv.setText(s);
    }

    @UI
    private void showxc(String s) {
        aLong = System.currentTimeMillis();
        TextView tv = findViewById(R.id.content);
        tv.setText(s);
    }

    @UI(repate = true, delay = 2000)
    public void main() {
        System.out.println("Thread主线程2 " + java.lang.Thread.currentThread().getId());
    }

    @CancelThread(value = {"主线程", "子线程"})
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
