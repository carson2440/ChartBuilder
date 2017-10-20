package com.carson.chartbuilder;

import android.graphics.Canvas;
import android.graphics.Color;

import com.github.mikephil.charting.animation.ChartAnimator;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.renderer.LineChartRenderer;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.util.ArrayList;

/**
 * Created by carson on 2017/10/18.
 * 实现在一个LineDataSet 下分段画线。
 */

public class BMFLineChartRenderer extends LineChartRenderer {

    public BMFLineChartRenderer(LineDataProvider chart, ChartAnimator animator, ViewPortHandler viewPortHandler) {
        super(chart, animator, viewPortHandler);
    }

    @Override
    protected void drawDataSet(Canvas c, ILineDataSet dataSet) {
        super.drawDataSet(c, dataSet);
    }

    /**
     * 遍历一次,过滤掉无效点（Null点）
     * 实现曲线分段
     */
    @Override
    protected void drawCubicBezier(ILineDataSet dataSet) {
        ArrayList<ArrayList<Entry>> subSets = new ArrayList<>();
        ArrayList<Entry> values = new ArrayList<>();
        for (int i = 0; i < dataSet.getEntryCount(); i++) {
            Entry entry = dataSet.getEntryForIndex(i);
            if (Float.isNaN(entry.getY())) {
                if (!values.isEmpty()) {
                    subSets.add(values);
                    values = new ArrayList<>();
                }
            } else {
                values.add(entry);
            }
        }
        if (!values.isEmpty()) {
            subSets.add(values);
        }

        for (int i = 0; i < subSets.size(); i++) {
            LineDataSet user = (LineDataSet) dataSet;
            LineDataSet setNew = new LineDataSet(subSets.get(i), user.getLabel());
            setNew.setDrawCircles(user.isDrawCirclesEnabled());//是否画点
            setNew.setColors(user.getColors());
            setNew.setCircleColor(user.getCircleColor(0));
            setNew.setCircleRadius(user.getCircleRadius());
            setNew.setDrawCircleHole(user.isDrawCircleHoleEnabled());
            setNew.setLineWidth(user.getLineWidth());
            setNew.setDrawFilled(user.isDrawFilledEnabled());//填充
            setNew.setDrawHorizontalHighlightIndicator(user.isHorizontalHighlightIndicatorEnabled());
            setNew.setMode(user.getMode());
            setNew.setCubicIntensity(user.getCubicIntensity());
            setNew.setFillAlpha(user.getFillAlpha());
            setNew.setHighlightEnabled(user.isHighlightEnabled());
            setNew.setFormLineDashEffect(user.getFormLineDashEffect());
            super.drawCubicBezier(setNew);
        }
    }
}

