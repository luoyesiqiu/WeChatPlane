package com.luoye.wechatplane;

import android.graphics.*;
import android.view.GestureDetector.*;
import android.view.*;
import android.view.View.*;
import android.util.*;

//主角类
public class Hero {
    float x, y;
    private float tempX, tempY;
    private Paint paint;
    private Bitmap[] back;
    private int backIndex = 0;
    static int score = 0;
    boolean isDead = false;
    int backWidth, backHeight;

    Hero(int x, int y, Bitmap[] back) {
        this.x = x;
        this.y = y;
        this.back = back;
        paint = new Paint();
        paint.setAntiAlias(true);
        backWidth = back[0].getWidth();
        backHeight = back[0].getHeight();
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(back[backIndex], this.x, this.y, paint);
    }

    public void logic() {
        backIndex++;
        if (backIndex > back.length - 1) {
            backIndex = 0;
        }

    }

    public boolean onDown(MotionEvent p1) {
        //点击屏幕时记录坐标
        tempX = p1.getX();
        tempY = p1.getY();
        return false;
    }

    //p3，p4为x，y坐标上的移动速度
    public boolean onFling(MotionEvent p1, MotionEvent p2, float p3, float p4) {

        return false;
    }

    //p3,p4为x,y坐标上的移动距离，相对于onDown()触发时的坐标
    public boolean onScroll(MotionEvent p1, MotionEvent p2, float p3, float p4) {
        if (p1.getAction() == MotionEvent.ACTION_DOWN) {
            tempX -= p3;
            tempY -= p4;
            //判断是否点在飞机上
            if (tempX >= x && tempY >= y && tempX <= x + this.backWidth && tempY <= y + this.backHeight) {
                x -= p3;
                y -= p4;
            }

        }
        //————边界判断
        if (this.x < 0) {
            x = 0;
        } else if (this.y < 0) {
            y = 0;
        } else if ((this.x + backWidth) > MainSurface.sw) {
            x = MainSurface.sw - backWidth;
        } else if ((this.y + backHeight) > MainSurface.sh) {
            y = MainSurface.sh - backHeight;
        }
        //————
        return false;
    }

}
