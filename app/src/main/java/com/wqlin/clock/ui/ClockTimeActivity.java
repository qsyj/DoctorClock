package com.wqlin.clock.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import com.wqlin.clock.utils.PrefUtils;
import com.wqlin.clock.widget.Bar;

import java.util.ArrayList;
import java.util.List;

public class ClockTimeActivity extends AppCompatActivity {

    private Bar mBar;
    private RecyclerView rvList;
    private List<ClockTimeEntity> mClockTimeList;
    private List<BaseTimeEntity> data=new ArrayList<>();
    private ClockTimeAdapter mAdapter;

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
                        showTimeDialog(0,0,0);
                        break;
                }
            }
        });

    }

    private void showTimeDialog(int hourOfDay, int minute, int second) {
        TimePickerDialog dialog = TimePickerDialog.newInstance(new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {

            }
        }, hourOfDay, minute, second, true);
        dialog.show(getFragmentManager(), "time");
    }
    private void initData() {
        mClockTimeList = getClockTime();
        int size = mClockTimeList.size();
        ClockTimeEntity resetEntity = null;
        ClockTimeEntity workEntity = null;
        ClockTimeEntity onDutyEntity = null;
        if (mClockTimeList != null&&size>0) {
            for (ClockTimeEntity entity:
                    mClockTimeList) {
                switch (entity.getSeletMode()) {
                    case DayEntity.MODE_DATE_RESET:
                        resetEntity = entity;
                        break;
                    case DayEntity.MODE_DATE_WORK:
                        workEntity = entity;
                        break;
                    case DayEntity.MODE_DATE_ON_DUTY:
                        onDutyEntity = entity;
                        break;
                }
            }
        }
        data.clear();
        addTime(data,DayEntity.MODE_DATE_RESET,resetEntity);
        addTime(data,DayEntity.MODE_DATE_WORK,workEntity);
        addTime(data,DayEntity.MODE_DATE_ON_DUTY,onDutyEntity);
    }

    private void init() {
        mAdapter.setNewData(data);
    }

    private void addTime(List<BaseTimeEntity> data,int mode, ClockTimeEntity clockTimeEntity) {
        int count = 0;
        List<TimeEntity> list = null;
        if (clockTimeEntity != null) {
            list= clockTimeEntity.getTimeList();
            count = list.size();
        }
        data.add( new HeaderTimeEntity(mode,count));
        if (list != null && list.size() > 0) {
            data.addAll(list);
        }
    }
    private List<ClockTimeEntity> getClockTime() {
        List<ClockTimeEntity> clockTimeList = null;
        String jsonStr = PrefUtils.getString(this, getKey());
        if (!TextUtils.isEmpty(jsonStr)) {
            clockTimeList= JSONObject.parseArray(jsonStr, ClockTimeEntity.class);
        }
        if (clockTimeList == null) {
            clockTimeList = new ArrayList<>();
        }
        return clockTimeList;
    }

    private String getKey() {
        return "clock_time";
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
                    helper.addOnClickListener(R.id.tv_add_clock_time);
                    break;
                case BaseTimeEntity.TYPE_ITEM_TIME:

                    break;
            }
        }
    }
}
