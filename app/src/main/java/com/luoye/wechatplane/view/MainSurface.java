package com.luoye.wechatplane.view;

import android.media.SoundPool;
import android.view.*;
import android.content.*;

import java.io.*;

import android.graphics.*;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

import android.view.View.*;
import android.view.GestureDetector.*;
import android.app.*;
import android.os.*;

import com.luoye.wechatplane.ator.BackGround;
import com.luoye.wechatplane.ator.BaoZha;
import com.luoye.wechatplane.ator.Const;
import com.luoye.wechatplane.ator.DaDiJi;
import com.luoye.wechatplane.ator.Hero;
import com.luoye.wechatplane.ator.Plane;
import com.luoye.wechatplane.ator.XiaoDiJi;
import com.luoye.wechatplane.ator.ZhongDiJi;
import com.luoye.wechatplane.ator.ZiDan;
import com.luoye.wechatplane.res.AssetUtils;
import com.luoye.wechatplane.util.BitmapUtils;
import com.luoye.wechatplane.util.Logger;

public class MainSurface extends SurfaceView implements
        SurfaceHolder.Callback, Runnable, OnGestureListener, OnTouchListener {

    private static final String TAG = MainSurface.class.getSimpleName();
    //游戏状态:0没开始，1游戏中，2游戏结束
    private final byte G_UNSTART = 0, G_ING = 1, G_OVER = 2;
    private SoundPool soundPool = null;
    private int shootId;
    private int exlosionId;
    GestureDetector gd;
    private Context context;
    private SurfaceHolder sfh;
    private Paint paint;
    //两张背景图
    private Bitmap backBmp1, backBmp2;
    //全部的飞机背景图
    private Bitmap planeBmp;
    //英雄背景图
    private Bitmap[] heroBmp;
    //子弹的背景图
    private Bitmap zidanBmp;
    //敌机的背景图
    private Bitmap xiaodijiBmp, zhongdijiBmp, dadijiBmp;
    //爆炸背景图
    private Bitmap bao1Bmp, bao2Bmp, bao3Bmp;

    //烟雾的背景图……算了不写了
    //Bitmap yan1Bmp,yan2Bmp;
    //屏幕高和宽
    public static int sw, sh;
    //背景图类
    private BackGround bg;
    //英雄类
    private Hero hero;

    //子弹
    private List<ZiDan> veZiDan;
    //爆炸
    private List<BaoZha> veBaoZha;
    //敌机
    private List<Plane> enemyPlaneList;
    //画布类
    private Canvas canvas;
    //游戏状态
    private byte g_state;
    //设置创建子弹和各种敌机的时间
    private int zdTime = 3;
    private int xdjTime = 6;
    private int zdjTime = 80;
    private int ddjTime = 300;
    //用来计数
    private long countTime = 0;
    //控制刷新屏幕的循环是否继续
    private boolean flag = true;

    private Thread gameThread;
    private Handler soundHandler;

    public MainSurface(Context context) {
        super(context);
        this.context = context;
        //获取surfaceview控制器实例
        sfh = getHolder();
        sfh.addCallback(this);

    }

    public void loadRes() {
        heroBmp = new Bitmap[2];

        //从资源中获得位图对象
        backBmp1 = AssetUtils.getImageFromAssetsFile(context,"image/bg_01.png");
        backBmp2 = AssetUtils.getImageFromAssetsFile(context,"image/bg_02.png");
        //重要！从Assets文件夹中获取文件不会被拉伸。从而保证飞机，敌机的正常显示
        planeBmp = AssetUtils.getImageFromAssetsFile(context,"image/plane.png");

        //从plane.png这张图片的一部分获得英雄的位图对象
        heroBmp[0] = BitmapUtils.resize(planeBmp.createBitmap(planeBmp, 66, 168, 62, 68),1.5f);
        heroBmp[1] = BitmapUtils.resize(planeBmp.createBitmap(planeBmp, 2, 168, 62, 75),1.5f);

        //子弹的图
        zidanBmp = planeBmp.createBitmap(planeBmp, 112, 2, 9, 17);
        //敌机图
        xiaodijiBmp = planeBmp.createBitmap(planeBmp, 201, 88, 39, 27);
        zhongdijiBmp = planeBmp.createBitmap(planeBmp, 130, 2, 69, 89);
        dadijiBmp = planeBmp.createBitmap(planeBmp, 2, 2, 108, 164);

        //爆炸图
        bao1Bmp = planeBmp.createBitmap(planeBmp, 216, 117, 26, 26);
        bao2Bmp = planeBmp.createBitmap(planeBmp, 144, 93, 38, 39);
        bao3Bmp = planeBmp.createBitmap(planeBmp, 201, 44, 40, 42);
        //烟雾图
        //yan1Bmp=planeBmp.createBitmap(planeBmp, 201,2, 48, 20);
        //yan2Bmp=planeBmp.createBitmap(planeBmp, 201, 24, 48, 18);

    }

    public void surfaceCreated(SurfaceHolder p1) {
        //获取屏幕大小
        sw = getWidth();
        sh = getHeight();

        //加载资源
        loadRes();

        veZiDan = new CopyOnWriteArrayList<>();
        veBaoZha = new CopyOnWriteArrayList<>();

        enemyPlaneList = new CopyOnWriteArrayList<>();

        bg = new BackGround(backBmp1, backBmp2);
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setTextSize(28.0f);
        hero = new Hero(sw / 2 - heroBmp[0].getWidth() / 2, sh - heroBmp[0].getHeight() * 3, heroBmp);

        //保持开启屏幕
        this.setKeepScreenOn(true);

        soundPool = new SoundPool.Builder().build();
        try {
            exlosionId = soundPool.load(context.getAssets().openFd("sound/explosion.mp3"), 0);
            shootId = soundPool.load(context.getAssets().openFd("sound/shoot.mp3"), 0);
        } catch (IOException e) {
            e.printStackTrace();
        }

        gd = new GestureDetector(context,this);
        this.setLongClickable(true);//重要
        this.setOnTouchListener(this);
        g_state = G_ING;
        //线程初始化，放在此处。避免最小化又打开游戏时又开启一个线程
        gameThread = new Thread(this);
        gameThread.start();
        HandlerThread soundHandlerThread = new HandlerThread("sound");
        soundHandlerThread.start();
        soundHandler = new Handler(soundHandlerThread.getLooper());
    }

    private void playSound(final int id) {
        soundHandler.post(() -> {
            soundPool.play(id, 0.5f, 1, 0, 0, 2f);
        });
    }

    Handler handler = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                Dialog dl = new AlertDialog.Builder(context)
                        .setMessage("游戏已结束，是否要继续？")
                        .setCancelable(false)
                        .setNegativeButton("否", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface p1, int p2) {
                                //退出游戏
                                System.exit(0);
                            }
                        })
                        .setPositiveButton("是", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface p1, int p2) {
                                //游戏重新开始，一切初始化
                                hero.isDead = false;
                                hero.x = sw / 2 - heroBmp[0].getWidth() / 2;
                                hero.y = sh - heroBmp[0].getHeight() * 3;
                                flag = true;
                                countTime = 0;
                                enemyPlaneList.clear();
                                veZiDan.clear();
                                veBaoZha.clear();
                                bg.resetBaseLine();
                                Hero.score = 0;
                                gameThread = new Thread(MainSurface.this);
                                gameThread.start();
                            }

                        })
                        .create();
                dl.show();
                super.handleMessage(msg);
            }
        }
    };

    /*绘图方法*/
    public void draw() {
        bg.draw(canvas);
        bg.drawScore(canvas);
        //英雄没死的时候绘图
        if (!hero.isDead) {
            hero.draw(canvas);
            //画子弹
            for (int i = 0; i < veZiDan.size(); i++)
                veZiDan.get(i).draw(canvas);
        }
        //死了，就向Handler发送消息
        else {
            //等于false，停止run方法下的循环。
            flag = false;
            soundPool.pause(shootId);
            soundPool.pause(exlosionId);
            handler.sendEmptyMessage(0);

        }

        //绘制敌机
        for (Plane plane : enemyPlaneList) {
            plane.draw(canvas);
        }

        //绘制爆炸
        for (int i = 0; i < veBaoZha.size(); i++)
            veBaoZha.get(i).draw(canvas);
    }

    /*逻辑方法*/
    public void logic() {

        if (!hero.isDead) {
            bg.logic();
            hero.logic();
            //子弹逻辑
            for (int i = 0; i < veZiDan.size(); i++) {
                ZiDan zd = veZiDan.get(i);
                if (zd.isDead)
                    veZiDan.remove(i);
                else
                    zd.logic();
            }

            //敌机逻辑敌机
            for (int i = 0; i < enemyPlaneList.size(); i++) {
                Plane enemyPlane = enemyPlaneList.get(i);

                if (enemyPlane.isDead) {
                    enemyPlaneList.remove(i);
                } else if (enemyPlane.getHp() <= 0) {
                    if (enemyPlane instanceof XiaoDiJi) {
                        Hero.score += 1;
                    } else if (enemyPlane instanceof ZhongDiJi) {
                        Hero.score += 10;
                    } else if (enemyPlane instanceof DaDiJi) {
                        Hero.score += 20;
                    }
                    enemyPlaneList.remove(i);


                } else {
                    enemyPlane.logic();
                }

            }

            //爆炸
            for (int i = 0; i < veBaoZha.size(); i++) {
                BaoZha bz = veBaoZha.get(i);
                if (bz.isDead)
                    veBaoZha.remove(i);
                else
                    bz.logic();

            }
            countTime++;
            //到时间就创建子弹、敌机
            if (countTime % zdTime == 0) {
                playSound(shootId);
                veZiDan.add(new ZiDan(zidanBmp, hero));
            }
            if (countTime % xdjTime == 0)
                enemyPlaneList.add(new XiaoDiJi(xiaodijiBmp));
            if (countTime % zdjTime == 0)
                enemyPlaneList.add(new ZhongDiJi(zhongdijiBmp));
            if (countTime % ddjTime == 0)
                enemyPlaneList.add(new DaDiJi(dadijiBmp));

            /*这里判断子弹是否击中敌机*/
            for (int i = 0; i < veZiDan.size(); i++) {
                for (int j = 0; j < enemyPlaneList.size(); j++) {
                    //如果子弹击中敌机
                    if (veZiDan.get(i).isHit(enemyPlaneList.get(j))) {
                        //在这里千万不要移除子弹元素,不然会出错，设置它的消亡标识就好
                        if (enemyPlaneList.get(j) instanceof XiaoDiJi) {
                            veBaoZha.add(new BaoZha(bao1Bmp, enemyPlaneList.get(j)));
                        } else if (enemyPlaneList.get(j) instanceof ZhongDiJi) {
                            veBaoZha.add(new BaoZha(bao2Bmp, enemyPlaneList.get(j)));
                        } else if (enemyPlaneList.get(j) instanceof DaDiJi) {
                            veBaoZha.add(new BaoZha(bao3Bmp, enemyPlaneList.get(j)));
                        }
                        playSound(exlosionId);
                        enemyPlaneList.get(j).hpDown();
                        veZiDan.get(i).isDead = true;
                    }

                }
            }
            /*以下判断敌机是否碰到英雄*/
            for (int i = 0; i < enemyPlaneList.size(); i++) {
                if (enemyPlaneList.get(i).isHit(hero)) {
                    hero.isDead = true;
                }
            }

        }

    }//logic 方法结尾

    @Override
    public void run() {
        while (flag) {
            canvas = sfh.lockCanvas();
            //如果画布不为空时绘图
            if (canvas != null) {
                this.draw();
                this.logic();
            }
            try {
                Thread.sleep(30);
            } catch (InterruptedException e) {
            } finally {
                //如果画布不为空时提交画布
                if (canvas != null) {
                    sfh.unlockCanvasAndPost(canvas);
                }
            }
        }

    }

    @Override
    public void surfaceChanged(SurfaceHolder p1, int p2, int p3, int p4) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder p1) {
        flag = false;
    }


    @Override
    public boolean onDown(MotionEvent p1) {
        Logger.d(Const.LOG_TAG,"%s","onDown");
        hero.onDown(p1);
        return false;
    }

    @Override
    public void onShowPress(MotionEvent p1) {
    }

    @Override
    public boolean onSingleTapUp(MotionEvent p1) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent p1, MotionEvent p2, float p3, float p4) {
        Logger.d(Const.LOG_TAG,"%s","onScroll");

        hero.onScroll(p1, p2, p3, p4);
        return false;
    }

    @Override
    public void onLongPress(MotionEvent p1) {
    }

    @Override
    public boolean onFling(MotionEvent p1, MotionEvent p2, float p3, float p4) {
        Logger.d(Const.LOG_TAG,"%s %f %f","onFling",p3,p4);

        hero.onFling(p1, p2, p3, p4);
        return false;
    }

    @Override
    public boolean onTouch(View p1, MotionEvent p2) {
        return gd.onTouchEvent(p2);
    }

}