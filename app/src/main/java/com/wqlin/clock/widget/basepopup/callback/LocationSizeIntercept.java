package com.wqlin.clock.widget.basepopup.callback;


import com.wqlin.clock.widget.basepopup.entity.LocationConsumer;
import com.wqlin.clock.widget.basepopup.entity.LocationSizeInfo;

/**
 * Created by wqlin on 2017/9/17.
 * 当BasePopupWindow执行getShowLocation() 没有设置宽高和返回location时 会调用mLocationSizeIntercept getLocationSize() <p>
 * 从而拦截并去修改popupwindow的location width height等信息
 */

public interface LocationSizeIntercept {
    LocationSizeInfo getLocationSize(LocationConsumer consumer);
}
