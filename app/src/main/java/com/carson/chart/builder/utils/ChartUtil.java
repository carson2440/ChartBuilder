package com.carson.chart.builder.utils;

import android.content.Context;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by carson on 2017/10/18.
 */

public class ChartUtil {

    public static int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static String getUnixTime(long time) {
        SimpleDateFormat format = new java.text.SimpleDateFormat("MM/dd HH:mm");
        return format.format(new Date(time * 1000));
    }
}
