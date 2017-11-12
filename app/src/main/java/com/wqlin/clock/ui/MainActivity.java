package com.wqlin.clock.ui;

import android.os.Bundle;

import com.wqlin.clock.R;
import com.wqlin.clock.base.BaseActivity;

import java.util.Calendar;

public class MainActivity extends BaseActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        Calendar now = Calendar.getInstance();

    }

}
