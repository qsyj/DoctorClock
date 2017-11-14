package com.wqlin.clock.utils;

import com.wqlin.clock.entity.TimeEntity;

import java.util.Comparator;

/**
 * @author wangql
 * @email wangql@leleyuntech.com
 * @date 2017/11/14 9:31
 */
public class SortByTime implements Comparator<TimeEntity> {
    @Override
    public int compare(TimeEntity o1, TimeEntity o2) {
        long time1 = AppUtils.getTime(o1.getHour(),o1.getMinute(),o1.getMinute());
        long time2 = AppUtils.getTime(o2.getHour(),o2.getMinute(),o2.getMinute());
        if (time2-time1>0)
            return -1;
        if (time2-time1<0)
            return 1;
        return 0;
    }
}
