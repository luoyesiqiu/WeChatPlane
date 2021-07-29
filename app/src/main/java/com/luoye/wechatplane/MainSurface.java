package com.luoye.wechatplane;

import android.view.*;
import android.content.*;
import android.content.res.*;

import java.io.*;
import android.graphics.*;
import java.util.*;
import android.view.View.*;
import android.view.GestureDetector.*;
import android.app.*;
import android.os.*;

public class MainSurface extends SurfaceView implements
        SurfaceHolder.Callback, Runnable, OnGestureListener, OnTouchListener {
    GestureDetector gd;
    Context context;
    SurfaceHolder sfh;
    Paint paint;
    //两张背景图
    Bitmap backBmp1, backBmp2;
    //全部的飞机背景图
    Bitmap planeBmp;
    //英雄背景图
    Bitmap[] heroBmp;
    //子弹的背景图
    Bitmap zidanBmp;
    //敌机的背景图
    Bitmap xiaodijiBmp, zhongdijiBmp, dadijiBmp;
    //爆炸背景图
    Bitmap bao1Bmp, bao2Bmp, bao3Bmp;

    //烟雾的背景图……算了不写了
    //Bitmap yan1Bmp,yan2Bmp;
    //屏幕高和宽
    static int sw, sh;
    //背景图类
    BackGround bg;
    //英雄类
    Hero hero;

    //子弹，敌机的动态数组
    Vector<ZiDan> veZiDan;
    Vector<XiaoDiJi> veXiaoDiJi;
    Vector<ZhongDiJi> veZhongDiJi;
    Vector<DaDiJi> veDaDiJi;
    Vector<BaoZha> veBaoZha;
    //画布类
    Canvas canvas;
    //游戏状态
    byte g_state;
    //游戏状态:0没开始，1游戏中，2游戏结束
    final byte G_UNSTART = 0, G_ING = 1, G_OVER = 2;
    //设置创建子弹和各种敌机的时间
    int zdTime = 3;
    int xdjTime = 6;
    int zdjTime = 80;
    int ddjTime = 300;
    //用来计数
    long countTime = 0;
    //控制刷新屏幕的循环是否继续
    boolean flag = true;

    Thread th;

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
        backBmp1 = getImageFromAssetsFile("image/bg_01.png");
        backBmp2 = getImageFromAssetsFile("image/bg_02.png");
        //重要！从Assets文件夹中获取文件不会被拉伸。从而保证飞机，敌机的正常显示
        planeBmp = getImageFromAssetsFile("image/plane.png");

        //从plane.png这张图片的一部分获得英雄的位图对象
        heroBmp[0] = planeBmp.createBitmap(planeBmp, 66, 168, 62, 68);
        heroBmp[1] = planeBmp.createBitmap(planeBmp, 2, 168, 62, 75);

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
        // TODO: Implement this method
        //获取屏幕大小
        this.sw = getWidth();
        this.sh = getHeight();

        //加载资源
        loadRes();

        veZiDan = new Vector<ZiDan>();
        veXiaoDiJi = new Vector<XiaoDiJi>();
        veZhongDiJi = new Vector<ZhongDiJi>();
        veDaDiJi = new Vector<DaDiJi>();
        veBaoZha = new Vector<BaoZha>();

        bg = new BackGround(backBmp1, backBmp2);
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setTextSize(28.0f);
        hero = new Hero(sw / 2 - heroBmp[0].getWidth() / 2, sh - heroBmp[0].getHeight() * 3, heroBmp);

        //保持开启屏幕
        this.setKeepScreenOn(true);

        gd = new GestureDetector(this);
        this.setLongClickable(true);//重要
        this.setOnTouchListener(this);
        g_state = G_ING;
        //线程初始化，放在此处。避免最小化又打开游戏时又开启一个线程
        th = new Thread(this);
        th.start();

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
                                veXiaoDiJi.removeAllElements();
                                veZhongDiJi.removeAllElements();
                                veDaDiJi.removeAllElements();
                                veZiDan.removeAllElements();
                                veBaoZha.removeAllElements();
                                bg.resetBaseLine();
                                Hero.score = 0;
                                th = new Thread(MainSurface.this);
                                th.start();
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
        if (hero.isDead != true) {
            hero.draw(canvas);
            //画子弹
            for (int i = 0; i < veZiDan.size(); i++)
                veZiDan.elementAt(i).draw(canvas);
        }
        //死了，就向Handler发送消息
        else {
            //等于false，停止run方法下的循环。
            flag = false;
            handler.sendEmptyMessage(0);

        }

        //画小敌机
        for (int i = 0; i < veXiaoDiJi.size(); i++)
            veXiaoDiJi.elementAt(i).draw(canvas);

        //画中敌机
        for (int i = 0; i < veZhongDiJi.size(); i++)
            veZhongDiJi.elementAt(i).draw(canvas);

        //画大敌机
        for (int i = 0; i < veDaDiJi.size(); i++)
            veDaDiJi.elementAt(i).draw(canvas);
        //画爆炸
        for (int i = 0; i < veBaoZha.size(); i++)
            veBaoZha.elementAt(i).draw(canvas);
    }

    /*逻辑方法*/
    public void logic() {

        if (hero.isDead != true) {
            bg.logic();
            hero.logic();
            //子弹逻辑
            for (int i = 0; i < veZiDan.size(); i++) {
                ZiDan zd = veZiDan.elementAt(i);
                if (zd.isDead)
                    veZiDan.removeElementAt(i);
                else
                    zd.logic();
            }

            //小敌机逻辑
            for (int i = 0; i < veXiaoDiJi.size(); i++) {
                XiaoDiJi xdj = veXiaoDiJi.elementAt(i);
                if (xdj.isDead)
                    veXiaoDiJi.removeElementAt(i);
                else if (xdj.hp == 0) {
                    Hero.score += 1;
                    veXiaoDiJi.removeElementAt(i);
                } else
                    xdj.logic();

            }
            //中敌机
            for (int i = 0; i < veZhongDiJi.size(); i++) {
                ZhongDiJi zdj = veZhongDiJi.elementAt(i);
                if (zdj.isDead)
                    veZhongDiJi.removeElementAt(i);
                else if (zdj.hp == 0) {
                    Hero.score += 10;
                    veZhongDiJi.removeElementAt(i);
                } else
                    zdj.logic();

            }
            //大敌机
            for (int i = 0; i < veDaDiJi.size(); i++) {
                DaDiJi ddj = veDaDiJi.elementAt(i);
                if (ddj.isDead)
                    veDaDiJi.removeElementAt(i);
                else if (ddj.hp == 0) {
                    Hero.score += 20;
                    veDaDiJi.removeElementAt(i);
                } else
                    ddj.logic();

            }

            //爆炸
            for (int i = 0; i < veBaoZha.size(); i++) {
                BaoZha bz = veBaoZha.elementAt(i);
                if (bz.isDead)
                    veBaoZha.removeElementAt(i);
                else
                    bz.logic();

            }
            countTime++;
            //到时间就创建子弹、敌机
            if (countTime % zdTime == 0)
                veZiDan.addElement(new ZiDan(zidanBmp, hero));
            if (countTime % xdjTime == 0)
                veXiaoDiJi.addElement(new XiaoDiJi(xiaodijiBmp));
            if (countTime % zdjTime == 0)
                veZhongDiJi.addElement(new ZhongDiJi(zhongdijiBmp));
            if (countTime % ddjTime == 0)
                veDaDiJi.addElement(new DaDiJi(dadijiBmp));

            /*这里判断子弹是否击中敌机*/
            for (int i = 0; i < veZiDan.size(); i++) {
                for (int j = 0; j < veXiaoDiJi.size(); j++) {
                    //如果子弹击中敌机
                    if (veZiDan.elementAt(i).isHit(veXiaoDiJi.elementAt(j))) {
                        //在这里千万不要移除子弹元素,不然会出错，设置它的消亡标识就好
                        veBaoZha.addElement(new BaoZha(bao1Bmp, veXiaoDiJi.elementAt(j)));
                        veZiDan.elementAt(i).isDead = true;
                    }
                }
                for (int j = 0; j < veZhongDiJi.size(); j++) {
                    if (veZiDan.elementAt(i).isHit(veZhongDiJi.elementAt(j))) {
                        veBaoZha.addElement(new BaoZha(bao2Bmp, veZhongDiJi.elementAt(j)));
                        veZiDan.elementAt(i).isDead = true;
                    }
                }
                for (int j = 0; j < veDaDiJi.size(); j++) {
                    if (veZiDan.elementAt(i).isHit(veDaDiJi.elementAt(j))) {
                        veBaoZha.addElement(new BaoZha(bao3Bmp, veDaDiJi.elementAt(j)));
                        veZiDan.elementAt(i).isDead = true;
                    }
                }
            }
            /*以下判断敌机是否碰到英雄*/
            for (int i = 0; i < veXiaoDiJi.size(); i++) {
                if (veXiaoDiJi.elementAt(i).isHit(hero)) {
                    hero.isDead = true;
                }
            }

            for (int i = 0; i < veZhongDiJi.size(); i++) {
                if (veZhongDiJi.elementAt(i).isHit(hero)) {
                    hero.isDead = true;
                }
            }
            for (int i = 0; i < veDaDiJi.size(); i++) {
                if (veDaDiJi.elementAt(i).isHit(hero)) {
                    hero.isDead = true;
                }
            }

        }

    }//logic 方法结尾

    @Override
    public void run() {
        while (flag) {
            try {
                canvas = sfh.lockCanvas();
                //如果画布不为空时绘图
                if (canvas != null) {
                    this.draw();
                    this.logic();
                }
                Thread.sleep(50);
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
        hero.onScroll(p1, p2, p3, p4);
        return false;
    }

    @Override
    public void onLongPress(MotionEvent p1) {
    }

    @Override
    public boolean onFling(MotionEvent p1, MotionEvent p2, float p3, float p4) {
        hero.onFling(p1, p2, p3, p4);
        return false;
    }

    @Override
    public boolean onTouch(View p1, MotionEvent p2) {
        return gd.onTouchEvent(p2);
    }

    //读取文件流，并把它转换成位图对象
    public Bitmap getImageFromAssetsFile(String fileName) {
        Bitmap image = null;
        AssetManager am = getResources().getAssets();
        try {
            InputStream is = am.open(fileName);
            image = BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }
}
