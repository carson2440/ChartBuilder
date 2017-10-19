package com.carson.chart.builder;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.carson.chart.builder.bean.ChartLine;
import com.carson.chart.builder.utils.ChartUtil;
import com.carson.chartbuilder.BMFLineChartRenderer;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * Created by carson on 2017/10/18.
 * Email:981385016@qq.com
 */
public class MainActivity extends AppCompatActivity implements OnChartGestureListener, OnChartValueSelectedListener {

    TextView chartMessage;
    LineChart chartLine;
    private long serverTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.chartLine = (LineChart) findViewById(R.id.chart_line);
        this.chartMessage = (TextView) findViewById(R.id.chart_message);
        initChartLine();
        buildChartData();
    }

    /**
     * 初始化图表相关属性（是否缩放,画line还是bar等）
     */
    private void initChartLine() {
        this.chartLine.setRenderer(new BMFLineChartRenderer(chartLine, chartLine.getAnimator(), chartLine.getViewPortHandler()));
        //控制图表和屏幕的padding
        //int offset = dp2px(this, 3);
        //chartLine.setViewPortOffsets(offset, 0f, offset, 0f);

        chartLine.getDescription().setEnabled(false);
        chartLine.setNoDataText("暂无数据");
        chartLine.setOnChartGestureListener(this);
        chartLine.setOnChartValueSelectedListener(this);
        //support scale flag
        chartLine.setTouchEnabled(true);
        chartLine.setDragEnabled(true);
        chartLine.setScaleXEnabled(true);
        chartLine.setScaleYEnabled(false);
        // if disabled, scaling can be done on x- and y-axis separately
        chartLine.setPinchZoom(false);
        chartLine.setAutoScaleMinMaxEnabled(false);
        //不显示右边的数字
        chartLine.getAxisRight().setEnabled(false);
        chartLine.getXAxis().setEnabled(false);

        YAxis leftAxis = chartLine.getAxisLeft();
//        leftAxis.setDrawLabels(false);
//        leftAxis.setDrawGridLines(false);
        Legend l = chartLine.getLegend();
        // modify the legend ...
        l.setForm(Legend.LegendForm.LINE);
        l.setEnabled(false);
    }

    /**
     * 从raw文件下读取json数据
     */
    private void buildChartData() {
        InputStream inputStream = getResources().openRawResource(R.raw.json);
        byte buffer[] = null;
        try {
            buffer = new byte[inputStream.available()];
            inputStream.read(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (buffer != null && buffer.length > 0) {
            String result = new String(buffer);
            JSONObject jsonObject = JSON.parseObject(result);
            int state = jsonObject.getIntValue("status");
            String dataList = jsonObject.getString("data_list");
            Map<String, List<ChartLine>> map = JSON.parseObject(dataList, new TypeReference<Map<String, List<ChartLine>>>() {
            });
            if (map != null) {
                List<ChartLine> list = map.get("42360");
                filterData(list);
            }
        }
    }

    /**
     * 分段数据,设置被画线的相关属性
     */
    private void filterData(List<ChartLine> list) {
        if (list == null) {
            return;
        }
        ArrayList<Entry> entryList = new ArrayList<Entry>();
        float x, y;
        for (ChartLine line : list) {
            x = Float.parseFloat(line.getT());
            if (TextUtils.isEmpty(line.getV())) {
                y = Float.NaN;
            } else {
                y = Float.parseFloat(line.getV());
            }
            entryList.add(new Entry(x, y, line.getT()));
        }
        LineDataSet dataSet = new LineDataSet(entryList, "chartLine");
        dataSet.setDrawCircles(true);//是否画点
//        dataSet.setColors(ColorTemplate.LINE_COLORS);
        dataSet.setCircleColor(Color.rgb(72, 192, 218));
        dataSet.setCircleRadius(1.2f);
        dataSet.setDrawCircleHole(false);
        dataSet.setLineWidth(1.1f);
        dataSet.setDrawFilled(true);//填充
        dataSet.setDrawHorizontalHighlightIndicator(false);
        dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);


        ArrayList<ILineDataSet> lineDataSets = new ArrayList<>();
        lineDataSets.add(dataSet);
        LineData data = new LineData(lineDataSets);
        data.setDrawValues(false);
        chartLine.setData(data);
        chartLine.animateX(300);

    }

    @Override
    public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

    }

    @Override
    public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
        chartMessage.setText(getString(R.string.app_name));
    }

    @Override
    public void onChartLongPressed(MotionEvent me) {

    }

    @Override
    public void onChartDoubleTapped(MotionEvent me) {

    }

    @Override
    public void onChartSingleTapped(MotionEvent me) {

    }

    @Override
    public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {

    }

    @Override
    public void onChartScale(MotionEvent me, float scaleX, float scaleY) {

    }

    @Override
    public void onChartTranslate(MotionEvent me, float dX, float dY) {

    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {
        long time = Long.parseLong(e.getData().toString());
        String timeStr = ChartUtil.getUnixTime(time);

        if (serverTime == 0 || time < serverTime) {
            if (Double.compare(e.getY(), chartLine.getAxisLeft().getAxisMinimum()) == 0) {
                chartMessage.setText(timeStr + " 无采集值");
            } else {
                chartMessage.setText(timeStr + " " + e.getY());
            }
        } else {
            chartMessage.setText(timeStr + " 未采集");
        }

    }

    @Override
    public void onNothingSelected() {

    }
}
