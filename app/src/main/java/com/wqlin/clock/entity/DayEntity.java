package com.wqlin.clock.entity;

/**
 * @author wangql
 * @email wangql@leleyuntech.com
 * @date 2017/11/10 15:52
 */
public class DayEntity {
    /**
     * 休息
     */
    public static final int MODE_DATE_RESET=0;
    /**
     * 正常上班
     */
    public static final int MODE_DATE_WORK=1;
    /**
     * 值班
     */
    public static final int MODE_DATE_ON_DUTY=2;
    /**
     * 自定义闹钟
     */
    public static final int MODE_DATE_CUSTOM_CLOCK=3;
    private int day;
    private int seletMode;

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getSeletMode() {
        return seletMode;
    }

    public void setSeletMode(int seletMode) {
        this.seletMode = seletMode;
    }
}
