package com.wqlin.clock.entity;

/**
 * @author wangql
 * @email wangql@leleyuntech.com
 * @date 2017/11/13 17:26
 */
public class HeaderTimeEntity extends BaseTimeEntity {
    private int count;

    public HeaderTimeEntity(int seletMode, int count) {
        super(seletMode);
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
