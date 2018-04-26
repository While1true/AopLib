package com.aoplibxx;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.aoplib.CancelThread;
import com.aoplib.NewThread;
import com.aoplib.UI;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Debounce extends AppCompatActivity  {

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
        viewById.setText("点击测试3秒内只显示一次吐司");
    }

    @com.aoplib.Debounce(value = 3000)
    public void toast() {
        Toast.makeText(this,"点击测试3秒内只显示一次吐司",Toast.LENGTH_SHORT).show();
    }


}
