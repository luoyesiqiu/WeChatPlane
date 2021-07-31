package com.luoye.wechatplane.ator;

import android.graphics.*;

import java.util.*;

import com.luoye.wechatplane.view.MainSurface;

/**
 * 小型敌机类
 */
public class SmallPlane extends EnemyPlane {
    public SmallPlane(Bitmap back, int speed) {
        super(back);
        this.back = back;
        setHp(1);
        this.speed = speed;
        isDead = false;
        backWidth = this.back.getWidth();
        backHeight = this.back.getHeight();
        rd = new Random();
        this.x = rd.nextInt(MainSurface.surfaceWidth - backWidth);
    }
}
