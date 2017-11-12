package com.wqlin.clock.entity;

import java.util.List;

/**
 * @author wangql
 * @email wangql@leleyuntech.com
 * @date 2017/11/10 15:51
 */
public class CalendarEntity {
    private int year;
    private int month;
    private List<DayEntity> dayList;

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public List<DayEntity> getDayList() {
        return dayList;
    }

    public void setDayList(List<DayEntity> dayList) {
        this.dayList = dayList;
    }
}
