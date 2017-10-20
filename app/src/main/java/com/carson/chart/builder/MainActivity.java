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
import com.carson.chartbuilder.BMFBarLineChartTouchListener;
import com.carson.chartbuilder.BMFLineChartRenderer;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
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
    BarChart chartBar;
    private long serverTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.chartMessage = (TextView) findViewById(R.id.chart_message);
        this.chartLine = (LineChart) findViewById(R.id.chart_line);
        this.chartBar = (BarChart) findViewById(R.id.chart_bar);
        initChartLine();
        locadChartLineData();

        initChartBar();
        locadChartBarData();
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
        chartLine.setOnTouchListener(new BMFBarLineChartTouchListener(chartLine, chartLine.getViewPortHandler().getMatrixTouch(), 3.f));
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
    private void locadChartLineData() {
        InputStream inputStream = getResources().openRawResource(R.raw.json_line);
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
                filterChartLineData(list);
            }
        }
    }

    /**
     * 分段数据,设置被画线的相关属性
     */
    private void filterChartLineData(List<ChartLine> list) {
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
/////////////////////////////////////////////////////////////////////////////

    private void initChartBar() {
//        chartBar.setViewPortOffsets(2f, ChartUtil.dp2px(this, 15), 0f, ChartUtil.dp2px(this, 15));
        chartBar.getDescription().setEnabled(false);
        chartBar.setNoDataText("暂无数据");
        chartBar.setOnChartGestureListener(this);
        chartBar.setOnTouchListener(new BMFBarLineChartTouchListener(chartBar, chartBar.getViewPortHandler().getMatrixTouch(), 3.f));
        chartBar.setOnChartValueSelectedListener(this);

        chartBar.setTouchEnabled(true);
        chartBar.setDragEnabled(true);
        chartBar.setScaleEnabled(true);
        // if disabled, scaling can be done on x- and y-axis separately
        chartBar.setPinchZoom(false);
        chartBar.setScaleXEnabled(true);


        chartBar.getAxisRight().setEnabled(false);//不显示右边的数字
//        chartBar.getXAxis().setEnabled(true);
        chartBar.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);

        YAxis leftAxis = chartBar.getAxisLeft();
//        leftAxis.setDrawLabels(false);
        leftAxis.setDrawGridLines(false);
        leftAxis.setDrawZeroLine(true);
        chartBar.setFitBars(true);
        Legend l = chartBar.getLegend();
        // modify the legend ...
        l.setForm(Legend.LegendForm.LINE);
        l.setEnabled(false);

    }

    private void locadChartBarData() {
        InputStream inputStream = getResources().openRawResource(R.raw.json_bar);
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
                List<ChartLine> list = map.get("50430");
                filterChartBarData(list);
            }
        }
    }

    private void filterChartBarData(List<ChartLine> list) {
        ArrayList<BarEntry> entries = new ArrayList<BarEntry>();
        Float x, y;
        for (int i = 0; i < list.size(); i++) {
            ChartLine line = list.get(i);
            x = Float.parseFloat(line.getT()) / (1000);
            if (TextUtils.isEmpty(line.getV())) {
                y = 0f;//无效数据
            } else {
                y = Float.parseFloat(line.getV());
            }
            entries.add(new BarEntry(i, y, line.getT()));
        }
        BarDataSet dataSet = new BarDataSet(entries, "ChartBar");
//    dataSet.setColors(ColorTemplate.SINGLE_COLORS);
//            dataSet.setHighLightAlpha(70);
        BarData data = new BarData(dataSet);
        data.setDrawValues(false);
        data.setBarWidth(0.65f);
        chartBar.setData(data);
        chartBar.animateY(300);
    }


    @Override
    public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

    }

    @Override
    public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
        chartMessage.setText(getString(R.string.app_name));
        chartLine.setDragEnabled(true);
    }

    @Override
    public void onChartLongPressed(MotionEvent e) {
        chartLine.setDragEnabled(false);
        Highlight h = chartLine.getHighlightByTouchPoint(e.getX(), e.getY());
        if (h != null) {
            chartLine.highlightValue(h, true);
        }
        chartMessage.setText("LongPressed. HI");
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
