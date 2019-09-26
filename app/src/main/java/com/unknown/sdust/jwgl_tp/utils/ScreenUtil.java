package com.unknown.sdust.jwgl_tp.utils;

import android.content.Context;

public class ScreenUtil {
    public static int dp2px(Context context,float dp){
        return Math.round(context.getResources().getDisplayMetrics().density * dp + 0.5f);
    }
}
