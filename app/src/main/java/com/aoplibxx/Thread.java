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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView viewById = findViewById(R.id.bt);
        viewById.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(v.getId());
                toast();
            }
        });
        viewById.setText("点击请求网络吧,延迟3秒显示结果\n @UI @NewThread都可以设置延迟时间");
        System.out.println("MainActivity2主线程 " + java.lang.Thread.currentThread().getId());
    }

    @NewThread
    @Debounce(value = 5000)
    public void toast() {
        System.out.println("MainActivity2子线程 " + java.lang.Thread.currentThread().getId());
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
                showxc("获取数据成功，准备延迟3s展示");
                show(stringBuffer.toString());
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        main();
    }

    @UI(delay = 3000)
    private void show(String s) {
        TextView tv = findViewById(R.id.content);
        System.out.println("开始解析展示" + (System.currentTimeMillis() - aLong));
        Spanned text = Html.fromHtml(s);
        System.out.println("结束解析展示" + (System.currentTimeMillis() - aLong));
        tv.setText(text);
    }

    @UI
    private void showxc(String s) {
        aLong = System.currentTimeMillis();
        TextView tv = findViewById(R.id.content);
        tv.setMovementMethod(ScrollingMovementMethod.getInstance());
        tv.setText(Html.fromHtml(s));
    }

    @UI
    public void main() {
        System.out.println("MainActivity2主线程2 " + java.lang.Thread.currentThread().getId());
    }

    @CancelThread
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
