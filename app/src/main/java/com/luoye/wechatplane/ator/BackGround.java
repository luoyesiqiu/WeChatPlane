package com.luoye.wechatplane.ator;

import android.graphics.*;
import android.util.*;

import com.luoye.wechatplane.util.Const;
import com.luoye.wechatplane.util.Logger;
import com.luoye.wechatplane.view.MainSurface;

import java.util.ArrayList;
import java.util.List;

/**
 * 背景类
 */
public class BackGround extends GameObject{
    private Bitmap back1, back2;
    private final int moveSpeed = 4;
    private List<Integer> backBaseLine = new ArrayList<>();
    private final int lineCount = 6;
    public BackGround(Bitmap back1, Bitmap back2) {
        this.back1 = back1;
        this.back2 = back2;
        paint.setColor(Color.BLACK);
        paint.setTextSize(MainSurface.surfaceWidth /20);
        resetBaseLine();
    }

    public void resetBaseLine(){

        for (int i = 0; i < lineCount; i++) {
            int baseLine = MainSurface.surfaceHeight - (i * back1.getHeight());
            backBaseLine.add(baseLine);
        }
        Log.d(Const.LOG_TAG, "init baseLine = " + backBaseLine.toString());
    }

    @Override
    public void draw(Canvas canvas) {

        for (int i = 0; i < backBaseLine.size(); i++) {
            //绘制首列
            canvas.drawBitmap(back1, 0, backBaseLine.get(i), paint);
            //绘制其他列
            for (int j = 1; j <= 2; j++) {
                canvas.drawBitmap(back2, back1.getWidth() * j, backBaseLine.get(i), paint);
            }
        }
        //绘制分数。要放绘制背景之后，不然会被背景覆盖
        canvas.drawText("Score:" + Hero.score, MainSurface.surfaceWidth /20, MainSurface.surfaceWidth /20, paint);
    }


    //背景逻辑
    @Override
    public void logic() {
        for (int i = 0; i < backBaseLine.size(); i++) {
            backBaseLine.set(i, backBaseLine.get(i) + moveSpeed);
        }

        if ((backBaseLine.get(0) - back1.getHeight()) >= MainSurface.surfaceHeight) {
            backBaseLine.remove(0);
            backBaseLine.add(backBaseLine.get(backBaseLine.size() - 1)- back1.getHeight());
            Logger.d(Const.LOG_TAG,"%s","onScroll");

        }

    }
}
