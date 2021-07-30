package com.luoye.wechatplane.ator;

import android.graphics.*;

import java.util.*;

import com.luoye.wechatplane.view.MainSurface;

/**
 * 小型敌机类
 */
public class XiaoDiJi extends Plane {
    public XiaoDiJi(Bitmap back) {
        this.back = back;
        setHp(1);
        speed = 10;
        isDead = false;
        backWidth = this.back.getWidth();
        backHeight = this.back.getHeight();
        rd = new Random();
        this.x = rd.nextInt(MainSurface.sw - backWidth);
        paint = new Paint();
        paint.setAntiAlias(true);
    }
}
