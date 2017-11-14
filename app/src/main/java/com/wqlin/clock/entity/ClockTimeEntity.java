package com.wqlin.clock.entity;

import java.util.List;

/**
 * @author wangql
 * @email wangql@leleyuntech.com
 * @date 2017/11/13 16:49
 */
public class ClockTimeEntity {
    private int seletMode;
    private List<TimeEntity> timeList;

    public ClockTimeEntity() {
    }

    public ClockTimeEntity(int seletMode, List<TimeEntity> timeList) {
        this.seletMode = seletMode;
        this.timeList = timeList;
    }

    public int getSeletMode() {
        return seletMode;
    }

    public void setSeletMode(int seletMode) {
        this.seletMode = seletMode;
    }

    public List<TimeEntity> getTimeList() {
        return timeList;
    }

    public void setTimeList(List<TimeEntity> timeList) {
        this.timeList = timeList;
    }
}
