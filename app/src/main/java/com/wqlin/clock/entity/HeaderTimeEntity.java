package com.wqlin.clock.entity;

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
        switch (seletMode) {
            case DayEntity.MODE_DATE_RESET:
                setTitle("休息");
                break;
            case DayEntity.MODE_DATE_WORK:
                setTitle("工作");
                break;
            case DayEntity.MODE_DATE_ON_DUTY:
                setTitle("值班");
                break;
            case DayEntity.MODE_DATE_CUSTOM_CLOCK:
                setTitle("休息");
                break;
        }
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
