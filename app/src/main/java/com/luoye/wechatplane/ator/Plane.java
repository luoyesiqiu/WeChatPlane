package com.luoye.wechatplane.ator;

import android.graphics.*;

import com.luoye.wechatplane.view.MainSurface;

import java.util.*;

/**
 * 敌机的抽象类,所有的敌机都要继承它
 */
public abstract class Plane extends GameObject {
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
    //随机数
    public Random rd;

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
