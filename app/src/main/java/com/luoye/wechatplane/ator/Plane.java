package com.luoye.wechatplane.ator;

import android.graphics.*;

import com.luoye.wechatplane.view.MainSurface;

import java.util.*;

/**
 * 敌机的抽象类,所有的敌机都要继承它
 */
public abstract class Plane {
    //背景图
    public Bitmap back;
    //敌机坐标
    public float x, y;
    //背景图的宽，高
    public int backWidth;
    public int backHeight;
    //敌机是否死亡
    public boolean isDead;
    //敌机的速度
    public float speed;
    //敌机的血
    private int hp;
    //画笔
    public Paint paint;
    //随机数
    public Random rd;

    public void draw(Canvas canvas) {
        canvas.drawBitmap(back, this.x, this.y, paint);
    }

    public void logic() {
        this.y += this.speed;
        //判断，超出屏幕就是消亡了 ?:是判断运算符
        this.isDead = (this.y >= (MainSurface.sh + backHeight)) ? true : false;
    }

    public boolean isHit(Hero hero) {
        if (
                ((this.y > hero.y) && (this.x > hero.x) && (this.x < (hero.x + hero.backWidth)) && (this.y < (hero.y + hero.backHeight)))
                        || ((this.x + this.backWidth) > hero.x && (this.x + this.backWidth) < (hero.x + hero.backWidth)
                        && ((this.y + this.backHeight) > hero.y && (this.y + this.backHeight) < (hero.y + hero.backHeight)))
        ) {
            return true;
        }
        else {
            return false;
        }
    }

    public void hpDown(){
        this.hp -= 1;
    }

    public  void setHp(int hp){
        this.hp = hp;
    }


    public int getHp(){
        return this.hp;
    }
}
