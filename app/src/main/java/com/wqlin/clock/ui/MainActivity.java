package com.wqlin.clock.ui;

import android.os.Bundle;
import android.view.View;

import com.wqlin.clock.R;
import com.wqlin.clock.base.BaseActivity;
import com.wqlin.clock.widget.Bar;

import java.util.Calendar;

public class MainActivity extends BaseActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initBar();
    }

    private void initView() {

    }

    private void initBar() {
        Bar bar = new Bar(this);
        bar.setTitle("排班表");
        bar.getToolbar().setNavigationIcon(null);
        bar.getmToolbarBack().setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_goto_time:
                ClockTimeActivity.start(this);
                break;
        }
    }
}
