package com.luoye.wechatplane.ator;

import com.luoye.wechatplane.view.MainSurface;

public abstract class EnemyPlane extends Plane {
    @Override
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

}
