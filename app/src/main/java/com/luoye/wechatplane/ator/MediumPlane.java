package com.luoye.wechatplane.ator;

import android.graphics.*;

import com.luoye.wechatplane.view.MainSurface;

import java.util.*;

/**
 * 中型敌机类
 */
public class MediumPlane extends EnemyPlane {

    public MediumPlane(Bitmap back, int speed) {
        super(back);
        this.back = back;
        this.speed = speed;
        setHp(10);
        isDead = false;
        backWidth = this.back.getWidth();
        backHeight = this.back.getHeight();
        rd = new Random();
        this.x = rd.nextInt(MainSurface.surfaceWidth - backWidth);
    }

}
