package com.luoye.wechatplane.util;

import android.graphics.Bitmap;
import android.graphics.Matrix;

public class BitmapUtils {
    public static Bitmap resize(Bitmap bitmap, float size)
    {
        Matrix matrix = new Matrix();
        matrix.postScale(size, size);

        return Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,true);
    }
}
