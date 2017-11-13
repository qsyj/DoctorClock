package com.wqlin.clock.entity;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * @author wangql
 * @email wangql@leleyuntech.com
 * @date 2017/11/13 17:06
 */
public class BaseTimeEntity implements MultiItemEntity {
    public static final int TYPE_ITEM_TIME = 1;
    public static final int TYPE_ITEM_HEAFER = 2;

    private int seletMode;
    private int itemType = TYPE_ITEM_HEAFER;

    public BaseTimeEntity() {
    }

    public BaseTimeEntity(int seletMode) {
        this.seletMode = seletMode;
    }

    public int getSeletMode() {
        return seletMode;
    }

    public void setSeletMode(int seletMode) {
        this.seletMode = seletMode;
    }

    @Override
    public int getItemType() {
        return itemType;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }
}
