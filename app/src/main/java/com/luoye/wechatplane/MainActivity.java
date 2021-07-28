package com.luoye.wechatplane;

import android.app.*;
import android.os.*;
import android.view.*;
import android.widget.*;

import java.io.*;

public class MainActivity extends Activity {

    private long curTime;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        getWindow().setFlags(1024, 1024);
        setContentView(new MainSurface(this));
    }

    @Override
    public void onBackPressed() {
        // TODO: Implement this method
        if (System.currentTimeMillis() - curTime > 2000) {
            curTime = System.currentTimeMillis();
            Toast.makeText(MainActivity.this, "再按一次返回键退出游戏", Toast.LENGTH_SHORT).show();
        } else {
            finish();
            System.exit(0);
        }
    }

}
