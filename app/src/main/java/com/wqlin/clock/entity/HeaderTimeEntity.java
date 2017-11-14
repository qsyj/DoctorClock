package com.wqlin.clock.entity;

import com.wqlin.clock.utils.AppUtils;

/**
 * @author wangql
 * @email wangql@leleyuntech.com
 * @date 2017/11/13 17:26
 */
public class HeaderTimeEntity extends BaseTimeEntity {
    private int count;
    private String title;

    public HeaderTimeEntity(int seletMode, int count) {
        super(seletMode);
        this.count = count;
        setTitle(AppUtils.getSelectModeText(seletMode));
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
