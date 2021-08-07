package com.luoye.wechatplane.ator;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.luoye.wechatplane.view.MainSurface;

public abstract class EnemyPlane extends Plane {
    protected Bitmap back;
    public EnemyPlane(Bitmap back) {
        this.back = back;
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawBitmap(back, this.x, this.y, paint);
    }

    @Override
    public void logic() {
        this.y += this.speed;
        //判断，超出屏幕就是消亡了 ?:是判断运算符
        this.isDead = (this.y >= (MainSurface.surfaceHeight + backHeight)) ? true : false;
    }

    public boolean isCollision(Hero hero) {
        if (this.x < hero.getX() + hero.backWidth &&
            this.x + this.backWidth > hero.getX() &&
            this.y < hero.getY() + hero.backHeight &&
                this.y + this.backHeight > hero.getY()

        ) {
            return true;
        }
        else {
            return false;
        }
    }

}
