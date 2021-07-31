package com.luoye.wechatplane.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class Utils {
    public static void goToGithub(Context context){
        Uri uri = Uri.parse(Const.REPO_URL);
        Intent it = new Intent(Intent.ACTION_VIEW, uri);
        context.startActivity(it);
    }
}
