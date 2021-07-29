package com.luoye.wechatplane;

import android.graphics.*;
import android.media.Image;

import java.util.*;

/**
 * 大型敌机类
 */
public class DaDiJi extends Plane {
    public DaDiJi(Bitmap back) {
        this.back = back;
        backWidth = this.back.getWidth();
        backHeight = this.back.getHeight();
        speed = 10;
        setHp(10);
        isDead = false;
        rd = new Random();
        this.x = rd.nextInt(MainSurface.sw - backWidth);
        paint = new Paint();
        paint.setAntiAlias(true);
    }

}

