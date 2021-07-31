package com.luoye.wechatplane.ator;

import android.graphics.*;

import com.luoye.wechatplane.view.MainSurface;

import java.util.*;

/**
 * 大型敌机类
 */
public class BigPlane extends EnemyPlane {
    public BigPlane(Bitmap back, int speed) {
        this.back = back;
        backWidth = this.back.getWidth();
        backHeight = this.back.getHeight();
        this.speed = speed;
        setHp(20);
        isDead = false;
        rd = new Random();
        this.x = rd.nextInt(MainSurface.sw - backWidth);
        paint = new Paint();
        paint.setAntiAlias(true);
    }

}

