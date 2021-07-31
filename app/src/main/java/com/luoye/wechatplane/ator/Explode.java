package com.luoye.wechatplane.ator;

import android.graphics.*;

/**
 * 敌机爆炸类
 */
public class Explode {
    private Bitmap back;
    private Paint paint;
    private int backWidth, backHeight;
    private Plane plane;
    public boolean isDead = false;

    public Explode(Bitmap back, Plane plane) {
        this.back = back;
        this.plane = plane;
        backWidth = this.back.getWidth();
        backHeight = this.back.getHeight();
        paint = new Paint();
        paint.setAntiAlias(true);
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(back, plane.x + plane.backWidth / 2 - backWidth / 2, plane.y + plane.backHeight / 2 - backHeight / 2, paint);
    }

    public void logic() {
        this.isDead = true;
    }
}
