package com.luoye.wechatplane;

import android.graphics.*;
import android.util.*;

/**
 * 敌机爆炸类
 */
public class BaoZha {
    private Bitmap back;
    private Paint paint;
    private int backWidth, backHeight;
    private Plane dj;
    boolean isDead = false;

    public BaoZha(Bitmap back, Plane dj) {
        this.back = back;
        this.dj = dj;
        backWidth = this.back.getWidth();
        backHeight = this.back.getHeight();
        paint = new Paint();
        paint.setAntiAlias(true);
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(back, dj.x + dj.backWidth / 2 - backWidth / 2, dj.y + dj.backHeight / 2 - backHeight / 2, paint);
    }

    public void logic() {
        this.isDead = true;
    }
}
