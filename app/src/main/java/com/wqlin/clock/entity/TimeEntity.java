package com.wqlin.clock.entity;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * @author wangql
 * @email wangql@leleyuntech.com
 * @date 2017/11/13 16:52
 */
public class TimeEntity extends BaseTimeEntity {
    private int hour;
    private int minute;
    private int second;
    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public int getSecond() {
        return second;
    }

    public void setSecond(int second) {
        this.second = second;
    }

    @Override
    public int getItemType() {
        return TYPE_ITEM_TIME;
    }
}
