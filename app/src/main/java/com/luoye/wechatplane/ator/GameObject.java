package com.luoye.wechatplane.ator;

import android.graphics.Paint;

public abstract class GameObject implements IDrawable,ILogical{
    private Paint paint;
    public GameObject(){
        this.paint = new Paint();
        //抗锯齿
        this.paint.setAntiAlias(true);
    }
}
