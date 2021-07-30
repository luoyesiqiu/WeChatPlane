package com.luoye.wechatplane.ator;

import android.graphics.*;

import com.luoye.wechatplane.view.MainSurface;

import java.util.*;

/**
 * 中型敌机类
 */
public class ZhongDiJi extends Plane {

    public ZhongDiJi(Bitmap back) {
        this.back = back;
        speed = 10;
        setHp(10);
        isDead = false;
        backWidth = this.back.getWidth();
        backHeight = this.back.getHeight();
        rd = new Random();
        this.x = rd.nextInt(MainSurface.sw - backWidth);
        paint = new Paint();
        paint.setAntiAlias(true);
    }

}
