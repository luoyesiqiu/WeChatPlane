package com.luoye.wechatplane.ator;

import com.luoye.wechatplane.view.MainSurface;

public abstract class EnemyPlane extends Plane {
    @Override
    public void logic() {
        this.y += this.speed;
        //判断，超出屏幕就是消亡了 ?:是判断运算符
        this.isDead = (this.y >= (MainSurface.sh + backHeight)) ? true : false;
    }

    public boolean isCollision(Hero hero) {
        if (this.x < hero.x + hero.backWidth &&
            this.x + this.backWidth > hero.x &&
            this.y < hero.y + hero.backHeight &&
                this.y + this.backHeight > hero.y

        ) {
            return true;
        }
        else {
            return false;
        }
    }

}
