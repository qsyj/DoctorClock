package com.wqlin.clock.ui;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wqlin.clock.R;
import com.wqlin.clock.base.BaseActivity;
import com.wqlin.clock.utils.AppUtils;
import com.wqlin.clock.widget.Bar;
import com.wqlin.clock.widget.DateSelectDialog;

public class MainActivity extends BaseActivity implements View.OnClickListener{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initBar();
        AppUtils.startClock(this);
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
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            AppUtils.gotoHome(this);
            return true;
        }
        return super.onKeyDown(keyCode, event);
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
