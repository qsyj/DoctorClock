package com.wqlin.clock.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;
import com.wqlin.clock.R;
import com.wqlin.clock.entity.BaseTimeEntity;
import com.wqlin.clock.entity.ClockTimeEntity;
import com.wqlin.clock.entity.DayEntity;
import com.wqlin.clock.entity.HeaderTimeEntity;
import com.wqlin.clock.entity.TimeEntity;
import com.wqlin.clock.utils.AppUtils;
import com.wqlin.clock.utils.PrefUtils;
import com.wqlin.clock.utils.SortByTime;
import com.wqlin.clock.widget.Bar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ClockTimeActivity extends AppCompatActivity {

    private Bar mBar;
    private RecyclerView rvList;
    private List<ClockTimeEntity> mClockTimeList;
    private List<BaseTimeEntity> data=new ArrayList<>();
    private ClockTimeAdapter mAdapter;
    private ClockTimeEntity mResetEntity;
    private ClockTimeEntity mWorkEntity;
    private ClockTimeEntity mOnDutyEntity;

    public static void start(Context context) {
        context.startActivity(new Intent(context, ClockTimeActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clock_time);
        initView();
        initBar();
        initData();
        init();
    }

    private void initView() {
        rvList = findViewById(R.id.rv_list);
        rvList.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new ClockTimeAdapter(data);
        rvList.setAdapter(mAdapter);
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.tv_add_clock_time:
                        HeaderTimeEntity headerTimeEntity= (HeaderTimeEntity) view.getTag();
                        showTimeDialog(headerTimeEntity,0,0,0);
                        break;
                }
            }
        });

        mAdapter.setOnItemChildLongClickListener(new BaseQuickAdapter.OnItemChildLongClickListener() {
            @Override
            public boolean onItemChildLongClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case  R.id.ll_clock_time:
                        showDeleteDialog((TimeEntity) view.getTag());
                        break;
                }
                return true;
            }
        });
    }

    private void showDeleteDialog(final TimeEntity timeEntity) {
        new AlertDialog.Builder(this)
                .setTitle("提示")
                .setMessage("你确定删除吗")
                .setPositiveButton("取消",null)
                .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        removeClock(timeEntity);
                    }
                })
                .create().show();
    }

    private void removeClock(final TimeEntity timeEntity) {
        switch (timeEntity.getSeletMode()) {
            case DayEntity.MODE_DATE_RESET:
                removeClock(mResetEntity.getTimeList(),timeEntity);
                break;
            case DayEntity.MODE_DATE_WORK:
                removeClock(mWorkEntity.getTimeList(),timeEntity);
                break;
            case DayEntity.MODE_DATE_ON_DUTY:
                removeClock(mOnDutyEntity.getTimeList(),timeEntity);
                break;
        }
        addAlltime();
        mAdapter.notifyDataSetChanged();
    }

    private void removeClock(List<TimeEntity> list,final TimeEntity timeEntity) {
        if (list==null||timeEntity==null)
            return;
        list.remove(timeEntity);
    }
    private void initBar() {
        mBar = new Bar(this);
        mBar.setTitle("闹钟列表");
        mBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void initData() {
        mClockTimeList = AppUtils.getClockTimeList(this);
        int size = mClockTimeList.size();
        mResetEntity = null;
        mWorkEntity = null;
        mOnDutyEntity = null;
        if (mClockTimeList != null&&size>0) {
            for (ClockTimeEntity entity:
                    mClockTimeList) {
                switch (entity.getSeletMode()) {
                    case DayEntity.MODE_DATE_RESET:
                        mResetEntity = entity;
                        break;
                    case DayEntity.MODE_DATE_WORK:
                        mWorkEntity = entity;
                        break;
                    case DayEntity.MODE_DATE_ON_DUTY:
                        mOnDutyEntity = entity;
                        break;
                }
            }
        }
        initClockTime();
        addAlltime();
    }

    private void initClockTime() {
        if (mResetEntity == null) {
            mResetEntity = new ClockTimeEntity();
        }
        initTime(DayEntity.MODE_DATE_RESET,mResetEntity);
        if (mWorkEntity == null) {
            mWorkEntity = new ClockTimeEntity();
        }
        initTime(DayEntity.MODE_DATE_WORK,mWorkEntity);
        if (mOnDutyEntity == null) {
            mOnDutyEntity = new ClockTimeEntity();
        }
        initTime(DayEntity.MODE_DATE_ON_DUTY,mOnDutyEntity);
    }

    private void initTime(int mode,ClockTimeEntity clockTimeEntity) {
        if (clockTimeEntity==null)
            return;
        clockTimeEntity.setSeletMode(mode);
        if (clockTimeEntity.getTimeList() == null) {
            clockTimeEntity.setTimeList(new ArrayList<TimeEntity>());
        }
    }
    private void init() {
        mAdapter.setNewData(data);
    }

    private void addAlltime() {
        data.clear();
        addTime(data,DayEntity.MODE_DATE_RESET, mResetEntity);
        addTime(data,DayEntity.MODE_DATE_WORK, mWorkEntity);
        addTime(data,DayEntity.MODE_DATE_ON_DUTY, mOnDutyEntity);
        saveData();
    }

    private void saveData() {
        mClockTimeList.clear();
        mClockTimeList.add(mResetEntity);
        mClockTimeList.add(mWorkEntity);
        mClockTimeList.add(mOnDutyEntity);
        AppUtils.putClockTimeList(this,mClockTimeList);
    }
    private void addTime(List<BaseTimeEntity> data,int mode, ClockTimeEntity clockTimeEntity) {
        int count = 0;
        List<TimeEntity> list = clockTimeEntity.getTimeList();
        count = list.size();
        sortTimeList(list);
        data.add( new HeaderTimeEntity(mode,count));
        if (list.size() > 0) {
            data.addAll(list);
        }
    }

    private void sortTimeList(List<TimeEntity> timeEntities) {
        Collections.sort(timeEntities, new SortByTime());
    }

    private void showTimeDialog(final HeaderTimeEntity headerTimeEntity,int hourOfDay, int minute, int second) {
        TimePickerDialog dialog = TimePickerDialog.newInstance(new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
                addClock(headerTimeEntity.getSeletMode(),hourOfDay,minute,second);
            }
        }, hourOfDay, minute, second, true);
        dialog.show(getFragmentManager(), "time");
    }

    private void addClock(int mode, int hourOfDay, int minute, int second) {
        switch (mode) {
            case DayEntity.MODE_DATE_RESET:
                addClock(mode, mResetEntity.getTimeList(), hourOfDay, minute, second);
                break;
            case DayEntity.MODE_DATE_WORK:
                addClock(mode, mWorkEntity.getTimeList(), hourOfDay, minute, second);
                break;
            case DayEntity.MODE_DATE_ON_DUTY:
                addClock(mode, mOnDutyEntity.getTimeList(), hourOfDay, minute, second);
                break;
        }
    }

    private void addClock(int mode, List<TimeEntity> timeList, int hourOfDay, int minute, int second) {
        if (AppUtils.containTime(AppUtils.getTime(hourOfDay, minute, second),timeList)) {
            return;
        }
        timeList.add(AppUtils.createTimeEntity(mode,hourOfDay,minute,second));
        addAlltime();
        mAdapter.notifyDataSetChanged();
    }

    class ClockTimeAdapter extends BaseMultiItemQuickAdapter<BaseTimeEntity, BaseViewHolder> {
        public ClockTimeAdapter(List<BaseTimeEntity> data) {
            super(data);
            addItemType(BaseTimeEntity.TYPE_ITEM_HEAFER,R.layout.item_clock_time_header);
            addItemType(BaseTimeEntity.TYPE_ITEM_TIME,R.layout.item_clock_time);
        }

        @Override
        protected void convert(BaseViewHolder helper, BaseTimeEntity item) {
            int type = item.getItemType();
            switch (type) {
                case BaseTimeEntity.TYPE_ITEM_HEAFER:
                    HeaderTimeEntity headerTimeEntity = (HeaderTimeEntity) item;
                    helper.setText(R.id.tv_time_title, headerTimeEntity.getTitle());
                    helper.setTag(R.id.tv_add_clock_time, headerTimeEntity);
                    helper.addOnClickListener(R.id.tv_add_clock_time);
                    break;
                case BaseTimeEntity.TYPE_ITEM_TIME:
                    TimeEntity timeEntity = (TimeEntity) item;
                    helper.setTag(R.id.ll_clock_time, timeEntity);
                    helper.setText(R.id.tv_clock_title, timeEntity.getTitle() + "闹钟");
                    helper.setText(R.id.tv_clock_time, AppUtils.getTimeFormat(timeEntity));
                    helper.addOnLongClickListener(R.id.ll_clock_time);
                    break;
            }
        }
    }
}
