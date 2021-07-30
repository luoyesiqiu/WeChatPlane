package com.luoye.wechatplane;

import android.graphics.*;
import android.util.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 背景类
 */
public class BackGround {
    private Bitmap back1, back2;
    private final int moveSpeed = 5;
    private Paint paint;
    private Canvas canvas;
    private List<Integer> backBaseLine = new ArrayList<>();
    private final int lineCount = 6;
    public BackGround(Bitmap back1, Bitmap back2) {
        this.back1 = back1;
        this.back2 = back2;
        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setTextSize(MainSurface.sw/20);
        paint.setAntiAlias(true);
        resetBaseLine();
    }

    public void resetBaseLine(){

        for (int i = 0; i < lineCount; i++) {
            int baseLine = MainSurface.sh - (i * back1.getHeight());
            backBaseLine.add(baseLine);
        }
        Log.d(Const.LOG_TAG, "init baseLine = " + backBaseLine.toString());
    }

    public void draw(Canvas canvas) {
        this.canvas = canvas;
        for (int i = 0; i < backBaseLine.size(); i++) {
            //绘制首列
            canvas.drawBitmap(back1, 0, backBaseLine.get(i), paint);
            //绘制其他列
            for (int j = 1; j <= 2; j++) {
                canvas.drawBitmap(back2, back1.getWidth() * j, backBaseLine.get(i), paint);
            }
        }
    }

    //画分数
    public void drawScore(Canvas canvas) {
        canvas.drawText("Score:" + Hero.score, MainSurface.sw/20, MainSurface.sw/20, paint);
    }

    //背景逻辑
    public void logic() {
        for (int i = 0; i < backBaseLine.size(); i++) {
            backBaseLine.set(i, backBaseLine.get(i) + moveSpeed);

            if ((backBaseLine.get(0) - back1.getHeight()) >= MainSurface.sh) {
                backBaseLine.remove(0);
                backBaseLine.add(backBaseLine.get(backBaseLine.size() - 1)- back1.getHeight());
                Logger.d(Const.LOG_TAG,"%s","onScroll");

            }
        }
    }
}
