package com.luoye.wechatplane;

import android.util.Log;

import java.io.Closeable;
import java.io.IOException;
import java.io.StringReader;

/**
 * @author luoyesiqiu
 * @date 2021-06-29
 */
public class Logger {
    private static boolean openLog = true;
    private enum LogType{
        DEBUG,WARN,ERROR
    }

    public static void setOpenLog(boolean open){
        openLog = open;
    }

    public static void d(String tag,String fmt,Object... args){
        splitLog(LogType.DEBUG,tag,String.format(fmt,args));
    }

    public static void w(String tag,String fmt,Object... args){
        splitLog(LogType.WARN,tag,String.format(fmt,args));
    }

    public static void e(String tag,String fmt,Object... args){
        splitLog(LogType.ERROR,tag,String.format(fmt,args));
    }

    private static void splitLog(LogType type,String tag, String msg) {
        if(!openLog){
            return;
        }
        StringReader stringReader = new StringReader(msg);
        char[] buf = new char[3072];
        int len = -1;
        try {
            while ((len = stringReader.read(buf)) != -1) {
                print(type,tag, new String(buf, 0, len));
            }
        } catch (Throwable e) {
            print(type,tag, "");
        }
        finally {
            close(stringReader);
        }
    }

    private static void print(LogType type,String tag, String msg) {
        switch (type){
            case DEBUG:
                Log.d(tag,msg);
                break;
            case WARN:
                Log.w(tag,msg);
                break;
            case ERROR:
                Log.e(tag,msg);
                break;
        }
    }

    private static void close(Closeable closeable){
        if(closeable != null){
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}