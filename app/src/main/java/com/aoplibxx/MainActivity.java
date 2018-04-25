package com.aoplibxx;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.aoplib.Debounce;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.click).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toast();
            }
        });
    }

    @Debounce(value = 5000,mark = "请求网络")
    public void toast(){
        System.out.println("toast");
        Toast.makeText(this,"cccccccc",Toast.LENGTH_LONG).show();
    }
}
