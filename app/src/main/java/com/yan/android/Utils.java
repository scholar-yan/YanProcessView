package com.yan.android;

import android.content.Context;

/**
 * 作者: 024347
 * 时间: 2021/6/9
 * 版本:
 * 说明:
 */
public class Utils {
    /**
     * 根据手机的分辨率由dp的单位转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 将sp值转换为px值
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }
}
