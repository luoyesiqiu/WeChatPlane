package com.luoye.wechatplane.ator;

import android.graphics.*;
import android.view.*;

import com.luoye.wechatplane.view.MainSurface;

//主角类
public class Hero extends Plane{
    private float x, y;
    private float tempX, tempY;
    private final Bitmap[] back;
    private int backIndex = 0;
    private static int score = 0;
    public boolean isDead = false;
    int backWidth, backHeight;

    public Hero(Bitmap[] back,int x, int y) {
        this.x = x;
        this.y = y;
        this.back = back;
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
        /*边界判断*/
        if (this.x < 0) {
            x = 0;
        } else if (this.y < 0) {
            y = 0;
        } else if ((this.x + backWidth) > MainSurface.surfaceWidth) {
            x = MainSurface.surfaceWidth - backWidth;
        } else if ((this.y + backHeight) > MainSurface.surfaceHeight) {
            y = MainSurface.surfaceHeight - backHeight;
        }
        return false;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public static int getScore(){
        return score;
    }

    public int addScore(int value){
        return score += value;
    }

    public int resetScore(){
        return score = 0;
    }

}
