package com.aoplibxx;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_);
    }

    public void Thread(View v) {
        startActivity(Thread.class);
    }

    public void Debounce(View v) {
        startActivity(Debounce.class);
    }


    private void startActivity(Class threadClass) {
        startActivity(new Intent(this, threadClass));
    }

}
