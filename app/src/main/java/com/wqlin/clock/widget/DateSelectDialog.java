package com.wqlin.clock.widget;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.chcts.wheelpicker.WheelPicker;
import com.wqlin.clock.R;

import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * author: wqlin
 * time: 2016/9/21 15:31
 * 返回码:RequestCode.RESULE_DATE_SELECT
 * <p>
 * Intent中 int参数对应的字段
 * <p>
 * 年: year 月: month 日:day
 */
public class DateSelectDialog extends DialogFragment implements WheelPicker.OnItemSelectedListener ,View.OnClickListener{
    WheelPicker wpYear;
    WheelPicker wpMonth;
    WheelPicker wpDay;
    public static String MODEL_BIRTHDAY = "MODEL_BIRTHDAY";

    public static String MODEL_UNLIMITED = "MODEL_UNLIMITED";

    public static String MODEL_NOT_DAY = "MODEL_NOT_DAY";
    private String MODE;
    private static final int MAX_CALENDAR_YEAR = 2100;
    private static final int MIN_CALENDAR_YEAR = 1896;
    private long MAX_DATE;
    private long MIN_DATE;
    private long SELECT_DATE;
    private int MAX_YEAR;
    private int MAX_YEAR_MONTH;//月份从0开始计算
    private int MAX_YEAR_DAY;
    private int MIN_YEAR;
    private int MIN_YEAR_MONTH;
    private int MIN_YEAR_DAY;
    private Calendar mCalendar;
    private int mSelectedYear;
    private int mSelectedMonth;
    private int mSelectedDay;
    private static final Map<String,WeakReference<List<String>>> DAYS = new HashMap<>();//使用弱引用
    private String YEAR_TAG;
    private String MONTH_TAG;
    private String DAY_TAG;
    private OnDaySelectedListener mDaySelectedListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.DateSelect);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        //设置dialog的 进出 动画
        getDialog().getWindow().setWindowAnimations(R.style.AnimBottom);
        View view = inflater.inflate(getLayoutResId(), container, false);
        initView(view);
        initListener();
        initData();
        return  view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.setCanceledOnTouchOutside(true);
            // 设置宽度为屏宽、靠近屏幕底部。
            Window window = dialog.getWindow();
            WindowManager.LayoutParams wlp = window.getAttributes();
            wlp.gravity = Gravity.BOTTOM;
            wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
            window.setAttributes(wlp);
        }
    }

    protected void initView(View view) {
        wpYear=view.findViewById(R.id.wp_year);
        wpMonth=view.findViewById(R.id.wp_month);
        wpDay=view.findViewById(R.id.wp_day);

        view.findViewById(R.id.btn_sure).setOnClickListener(this);
        view.findViewById(R.id.btn_cancel).setOnClickListener(this);
        view.findViewById(R.id.v_out).setOnClickListener(this);

        mCalendar = Calendar.getInstance();
        YEAR_TAG = getString(R.string.year);
        MONTH_TAG = getString(R.string.month);
        DAY_TAG = getString(R.string.day);
        setDateRange();

    }

    protected void initListener() {
        wpYear.setOnItemSelectedListener(this);
        wpMonth.setOnItemSelectedListener(this);
        wpDay.setOnItemSelectedListener(this);
    }

    protected void initData() {
        try {
            updateYears();
            updateSelectedYear();
            updateMonths(mSelectedYear);
            updateSelectedMonth();
            updateDays(mSelectedYear, mSelectedMonth);
            updateSelectedDay();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initialize(OnDaySelectedListener daySelectedListener,long minDate, long maxDate, long selectDate,String mode) {
        mDaySelectedListener = daySelectedListener;
        MIN_DATE = minDate;
        MAX_DATE = maxDate;
        SELECT_DATE = selectDate;
        MODE = mode;
    }
    private void setDateRange() {
        try {
            mCalendar = Calendar.getInstance();
            if (MODE.equals(MODEL_NOT_DAY)) {
                wpDay.setVisibility(View.GONE);
            }
            if (MIN_DATE < 0) {
                MIN_YEAR = MIN_CALENDAR_YEAR;
                MIN_YEAR_MONTH = 0;
                MIN_YEAR_DAY = 1;
            } else {
                mCalendar.setTimeInMillis(MIN_DATE);
                MIN_YEAR = mCalendar.get(Calendar.YEAR);
                MIN_YEAR_MONTH = mCalendar.get(Calendar.MONTH);
                MIN_YEAR_DAY = mCalendar.get(Calendar.DAY_OF_MONTH);
            }

            if (MAX_DATE > 0) {
                mCalendar.setTimeInMillis(MAX_DATE);
            } else {
                if (MODE.equals(MODEL_BIRTHDAY)) {
                    mCalendar = Calendar.getInstance();
                } else {
                    mCalendar.set(MAX_CALENDAR_YEAR,11,30);
                }
            }
            MAX_YEAR = mCalendar.get(Calendar.YEAR);
            MAX_YEAR_MONTH = mCalendar.get(Calendar.MONTH);
            MAX_YEAR_DAY = mCalendar.get(Calendar.DAY_OF_MONTH);
//            mCalendar.clear();
            if (SELECT_DATE > 0) {
                mCalendar.setTimeInMillis(SELECT_DATE);
                mSelectedYear = mCalendar.get(Calendar.YEAR);
                mSelectedMonth = mCalendar.get(Calendar.MONTH) + 1;
                mSelectedDay = mCalendar.get(Calendar.DAY_OF_MONTH);
//                Log.e("DateSelectActivity", "setDateRange mSelectedYear:" + mSelectedYear + ",selectMonth:" + mSelectedMonth + ",mSelectedDay:" + mSelectedDay + ",SELECT_DATE:" + SELECT_DATE);
            } else {
                mSelectedYear = MAX_YEAR;
                mSelectedMonth = MAX_YEAR_MONTH + 1;
                mSelectedDay = MAX_YEAR_DAY;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void updateYears() {
        try {
            List<String> data = new ArrayList<>();
            for (int i = MIN_YEAR; i <= MAX_YEAR; i++)
                data.add(i + YEAR_TAG);
            wpYear.setData(data);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void updateSelectedYear() {
        try {
            wpYear.setSelectedItemPosition(mSelectedYear - MIN_YEAR);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateMonths(int year) {
        try {
            List<String> data = new ArrayList<>();
            if (year != MAX_YEAR && year != MIN_YEAR) {
                for (int i = 1; i <= 12; i++)
                    data.add(i + MONTH_TAG);
            } else {
                if (MIN_YEAR == MAX_YEAR) {
                    for (int i = MIN_YEAR_MONTH + 1; i <= MAX_YEAR_MONTH + 1; i++) {
                        data.add(i + MONTH_TAG);
                    }
                    if (mSelectedMonth > MAX_YEAR_MONTH + 1) {
                        mSelectedMonth = MAX_YEAR_MONTH + 1;
                    }
                    if (mSelectedMonth < MAX_YEAR_MONTH + 1) {
                        mSelectedMonth = MIN_YEAR_MONTH + 1;
                    }
                } else {
                    if (year == MAX_YEAR) {
                        for (int i = 1; i <= MAX_YEAR_MONTH + 1; i++)
                            data.add(i + MONTH_TAG);
                        if (mSelectedMonth > MAX_YEAR_MONTH + 1) {
                            mSelectedMonth = MAX_YEAR_MONTH + 1;
                        }
                    } else {
                        for (int i = MIN_YEAR_MONTH + 1; i <= 12; i++)
                            data.add(i + MONTH_TAG);
                        if (mSelectedMonth < MAX_YEAR_MONTH + 1) {
                            mSelectedMonth = MIN_YEAR_MONTH + 1;
                        }
                    }
                }

            }
            wpMonth.setData(data);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void updateSelectedMonth() {
        try {
            wpMonth.setSelectedItemPosition(mSelectedMonth - 1);
            int startMonth = Integer.valueOf(String.valueOf(wpMonth.getData().get(0)).replace(MONTH_TAG, ""));
            onItemSelected(wpMonth, wpMonth.getData().get(mSelectedMonth - startMonth), mSelectedMonth - 1);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void updateDays(int year, int month) {
        try {
            int startDay = 1;
            int endDay = 31;
            if (year == MIN_YEAR && month == MIN_YEAR_MONTH + 1) {
                startDay = MIN_YEAR_DAY;
            }
            if (year == MAX_YEAR && month == MAX_YEAR_MONTH + 1) {
                endDay = MAX_YEAR_DAY;
            } else {
                mCalendar.set(Calendar.YEAR, year);
                mCalendar.set(Calendar.MONTH, month - 1);
                endDay = mCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);
            }
            List<String> data = null;
            WeakReference<List<String>> weak = DAYS.get(startDay + "_" + endDay);
            if ( weak!= null) {
                data= weak.get();
            }
            if (null == data) {
                data = new ArrayList<>();
                for (int i = startDay; i <= endDay; i++)
                    data.add(i + DAY_TAG);
                DAYS.put(startDay + "_" + endDay, new WeakReference<List<String>>(data));
            }
            if (mSelectedDay > endDay) {
                mSelectedDay = endDay;
            }
            if (mSelectedDay < startDay) {
                mSelectedDay = startDay;
            }
            wpDay.setData(data);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void updateSelectedDay() {
        try {
            int startDay = Integer.valueOf(String.valueOf(wpDay.getData().get(0)).replace(DAY_TAG, ""));
            wpDay.setSelectedItemPosition(mSelectedDay - startDay);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public int getCurrentYear() {
        try {
            return Integer.valueOf(String.valueOf(wpYear.getData().get(wpYear.getCurrentItemPosition())).replace(YEAR_TAG, ""));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public int getCurrentMonth() {
        try {
            return Integer.valueOf(String.valueOf(wpMonth.getData().get(wpMonth.getCurrentItemPosition())).replace(MONTH_TAG, ""));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public int getCurrentDay() {
        try {
            return Integer.valueOf(String.valueOf(wpDay.getData().get(wpDay.getCurrentItemPosition())).replace(DAY_TAG, ""));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_sure:
                notifyOnDateListener();
                dismiss();
                break;
            case R.id.v_out:
            case R.id.btn_cancel:
                dismiss();
                break;
        }
    }

    public void notifyOnDateListener() {
        if (mDaySelectedListener != null) {
            mDaySelectedListener.onDaySelected(mSelectedYear, mSelectedMonth, mSelectedDay);
        }
    }

    protected int getLayoutResId() {
        return R.layout.activity_date_select;
    }

    public static DateSelectDialog newInstance(OnDaySelectedListener daySelectedListener) {

        return DateSelectDialog.newInstance(daySelectedListener, -1L,-1L,-1L,MODEL_BIRTHDAY);
    }

    public static DateSelectDialog newInstance(OnDaySelectedListener daySelectedListener, String mode) {

        return DateSelectDialog.newInstance(daySelectedListener, -1L,-1L,-1L,mode);
    }

    public static DateSelectDialog newInstance(OnDaySelectedListener daySelectedListener, String mode,boolean isToday) {
        long selectDate = -1L;
        if (isToday) {
            selectDate=Calendar.getInstance().getTimeInMillis();
        }
        return DateSelectDialog.newInstance(daySelectedListener, -1L,-1L,selectDate,mode);
    }

    public static DateSelectDialog newInstance(OnDaySelectedListener daySelectedListener, long maxDate) {

        return DateSelectDialog.newInstance(daySelectedListener,-1L, maxDate, -1L,MODEL_UNLIMITED);
    }

    public static DateSelectDialog newInstance(OnDaySelectedListener daySelectedListener, int minYear, int minMonth, int minDay, int maxYear, int maxMonth, int maxDay,String mode) {

        return DateSelectDialog.newInstance(daySelectedListener, minYear, minMonth, minDay, maxYear, maxMonth, maxDay, 0, 0, 0,mode);
    }

    public static DateSelectDialog newInstance(OnDaySelectedListener daySelectedListener, int selectYear, int selectMonth, int selectDay) {

        return DateSelectDialog.newInstance(daySelectedListener, selectYear, selectMonth, selectDay,MODEL_BIRTHDAY);
    }
    public static DateSelectDialog newInstance(OnDaySelectedListener daySelectedListener, int selectYear, int selectMonth, int selectDay, String mode) {
        Calendar calendar = Calendar.getInstance();
        if (mode.equals(MODEL_UNLIMITED)) {
            calendar.set(MAX_CALENDAR_YEAR, 11, 30);
        }
        int maxYear = calendar.get(Calendar.YEAR);
        int maxMonth = calendar.get(Calendar.MONTH) + 1;
        int maxDay = calendar.get(Calendar.DAY_OF_MONTH);
//        Log.e("DateSelectActivity","toNextActivityForResult maxYear:"+maxYear+",maxMonth:"+maxMonth+",maxDay:"+maxDay);
        calendar.set(MIN_CALENDAR_YEAR, 0, 1);
        int minYear = calendar.get(Calendar.YEAR);
        int minMonth = calendar.get(Calendar.MONTH) + 1;
        int minDay = calendar.get(Calendar.DAY_OF_MONTH);
        return DateSelectDialog.newInstance(daySelectedListener,minYear, minMonth, minDay, maxYear, maxMonth, maxDay, selectYear, selectMonth, selectDay,mode);
    }
    public static DateSelectDialog newInstance(OnDaySelectedListener daySelectedListener, int minYear, int minMonth, int minDay, int maxYear,
                                               int maxMonth, int maxDay, int selectYear, int selectMonth, int selectDay,String mode) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(minYear, minMonth - 1, minDay);
        long min = calendar.getTimeInMillis();
        calendar.set(maxYear, maxMonth - 1, maxDay);
        long max = calendar.getTimeInMillis();
        calendar.set(selectYear, selectMonth - 1, selectDay);
        long select = calendar.getTimeInMillis();
//        Log.e("DateSelectActivity", "toNextActivityForResult selectYear:" + selectYear + ",selectMonth:" + selectMonth + ",selectDay:" + selectDay + ",select:" + select);
        if (min > max) {
            throw new InvalidParameterException("maxDate not should < minDate");
        }
        if (!(select <= max && select >= min)) {
            throw new InvalidParameterException("selectDate should minDate<=selectDate<=maxDate");
        }
        checkDate(minYear, minMonth, minDay);
        checkDate(maxYear, maxMonth, maxDay);
        checkDate(selectYear, selectMonth, selectDay);
        return DateSelectDialog.newInstance(daySelectedListener, min, max, select,mode);
    }

    public static DateSelectDialog newInstance(OnDaySelectedListener daySelectedListener, long minDate, long maxDate, long selectDate, String mode) {
        if (!(mode != null && (mode.equals(MODEL_UNLIMITED) || mode.equals(MODEL_BIRTHDAY) || mode.equals(MODEL_NOT_DAY)))) {
            mode=MODEL_BIRTHDAY;
        }
        DateSelectDialog dateSelectActivity = new DateSelectDialog();
        dateSelectActivity.initialize(daySelectedListener, minDate, maxDate, selectDate, mode);
        return dateSelectActivity;
    }

    private static void checkDate(int minYear, int minMonth, int minDay) {
        Calendar calendar = Calendar.getInstance();
        if (minYear > MAX_CALENDAR_YEAR) {
            throw new InvalidParameterException("minYear not should > " + MAX_CALENDAR_YEAR);
        }
        if (minYear < MIN_CALENDAR_YEAR) {
            throw new InvalidParameterException("minYear not should < " + MIN_CALENDAR_YEAR);
        }
        if (minMonth > 12) {
            throw new InvalidParameterException("minMonth not should > 12");
        }
        if (minMonth < 1) {
            throw new InvalidParameterException("minMonth not should < 1");
        }
        calendar.set(Calendar.YEAR, minYear);
        calendar.set(Calendar.MONTH, minMonth);
        int maximum = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        if (minDay > maximum) {
            throw new InvalidParameterException("if minYear=" + minYear + "&& minMonth=" + minMonth + "minDay not should > " + maximum);
        }
        if (minDay < 1) {
            throw new InvalidParameterException("minDay not should < 1");
        }
    }

    private int getData(String data, String tag) {
        return Integer.valueOf(data.replace(tag, ""));
    }

    @Override
    public void onItemSelected(WheelPicker picker, Object data, int position) {
        try {
            if (picker.getId() == R.id.wp_year) {
                mSelectedYear = getData((String) data, YEAR_TAG);
                updateMonths(mSelectedYear);
                updateSelectedMonth();
            } else if (picker.getId() == R.id.wp_month) {
                mSelectedMonth = getData((String) data, MONTH_TAG);
                updateDays(getCurrentYear(), mSelectedMonth);
                updateSelectedDay();
            } else {
                mSelectedDay = getData((String) data, DAY_TAG);
            }
//            String currentDate = getCurrentYear() + "-" + getCurrentMonth() + "-" + getCurrentDay();
//            Log.e("DateSelectActivity", "DateSelectActivity currentDate:" + currentDate);
//            Log.e("DateSelectActivity", "DateSelectActivity mSelectedYear:" + mSelectedYear+",mSelectedMonth:"+mSelectedMonth+",mSelectedDay:"+mSelectedDay);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public interface OnDaySelectedListener extends Serializable{
        void onDaySelected(int year, int month, int day);
    }
}