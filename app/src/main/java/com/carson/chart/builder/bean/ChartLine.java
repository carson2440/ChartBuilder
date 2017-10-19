package com.carson.chart.builder.bean;

/**
 * Created by carson on 2017/10/18.
 */

public class ChartLine {
    private String t;//x
    private String v;//y (the val may be null)
    private int s; //period

    public String getT() {
        return t;
    }

    public void setT(String t) {
        this.t = t;
    }

    public String getV() {
        return v;
    }

    public void setV(String v) {
        this.v = v;
    }

    public int getS() {
        return s;
    }

    public void setS(int s) {
        this.s = s;
    }
}

