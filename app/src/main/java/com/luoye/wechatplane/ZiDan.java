package com.luoye.wechatplane;

import android.graphics.*;
import android.util.*;

/**
 * 子弹类
 */
public class ZiDan {
    float x, y;
    Paint paint;
    boolean isDead = false;
    Bitmap back;
    float speed = 30.5f;
    static int backWidth, backHeight;

    public ZiDan(Bitmap back, Hero hero) {
        this.back = back;
        this.backWidth = back.getWidth();
        this.backHeight = back.getHeight();
        this.x = (hero.x + hero.backWidth / 2) - this.backWidth / 2;
        this.y = hero.y - this.backHeight;
        paint = new Paint();
        paint.setAntiAlias(true);
    }

    public void logic() {
        y -= speed;
        //判断，超出屏幕就是消亡了 ?:是判断运算符
        this.isDead = ((this.y + this.backHeight) <= 0) ? true : false;
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(back, x, y, paint);
    }

    //判断子弹是否击中敌机
    public boolean isHit(Plane plane) {
        if (((plane.y >= this.y) && (this.x >= plane.x) && (this.x <= plane.x + plane.backWidth))) {
            //击中,敌机血-1，下同
            plane.hpDown();
            return true;
        } else
            return false;
    }

}
