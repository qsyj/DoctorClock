package com.wqlin.clock.widget;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wdullaer.materialdatetimepicker.HapticFeedbackController;
import com.wdullaer.materialdatetimepicker.Utils;
import com.wdullaer.materialdatetimepicker.date.DatePickerController;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.date.DateRangeLimiter;
import com.wdullaer.materialdatetimepicker.date.DefaultDateRangeLimiter;
import com.wdullaer.materialdatetimepicker.date.MonthAdapter;
import com.wdullaer.materialdatetimepicker.date.MonthView;
import com.wqlin.clock.R;
import com.wqlin.clock.entity.CalendarEntity;
import com.wqlin.clock.entity.DayEntity;
import com.wqlin.clock.view.DoctorMonthView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

/**
 * @author wangql
 * @email wangql@leleyuntech.com
 * @date 2017/11/10 14:19
 */
public class CalendarView extends FrameLayout implements DatePickerController ,MonthView.OnDayClickListener ,View.OnClickListener,DoctorMonthView.DateSelectController{
    private LinearLayout llRoot;
    private TextView tvTitle ,tvSure;
    private DoctorMonthView viewMonth;

    private Calendar mCalendar = Utils.trimToMidnight(Calendar.getInstance(getTimeZone()));
    private HashSet<Calendar> highlightedDays = new HashSet<>();
    private HashSet<OnDateChangedListener> mListeners = new HashSet<>();
    private Map<Integer,DayEntity> mDayList = new HashMap<>();
    private HapticFeedbackController mHapticFeedbackController;
    private int mWeekStart = mCalendar.getFirstDayOfWeek();
    private int mAccentColor = -1;
    private boolean mThemeDark = false;
    private DefaultDateRangeLimiter mDefaultLimiter = new DefaultDateRangeLimiter();
    private DateRangeLimiter mDateRangeLimiter = mDefaultLimiter;
    private boolean mVibrate = true;
    private TimeZone mTimezone;
    private int mDateSelectMode = DayEntity.MODE_DATE_ON_DUTY;
    private CalendarEntity mCalendarEntity;
    private DoctorMonthView.MonthViewEntity mResetMonthViewEntity;
    private DoctorMonthView.MonthViewEntity mWorkMonthViewEntity;
    private DoctorMonthView.MonthViewEntity mOnDutyMonthViewEntity;
    private DoctorMonthView.MonthViewEntity mCustomClockMonthViewEntity;
    public CalendarView(@NonNull Context context) {
        this(context, null);
    }

    public CalendarView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CalendarView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.view_calendar, this);
        if (mAccentColor == -1) {
            mAccentColor = Utils.getAccentColorFromThemeIfAvailable(getContext());
        }

        Calendar now = Calendar.getInstance();
        initView();
        setDate(now.get(Calendar.YEAR),now.get(Calendar.MONTH),now.get(Calendar.DAY_OF_MONTH));
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (mHapticFeedbackController == null) {
            mHapticFeedbackController = new HapticFeedbackController(getContext());
        }
        mHapticFeedbackController.start();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mHapticFeedbackController != null) {
            mHapticFeedbackController.stop();
        }
    }

    private void initView() {
        llRoot = findViewById(R.id.ll_root);
        tvTitle = findViewById(R.id.tv_title);
        tvSure = findViewById(R.id.tv_sure);
        tvSure.setOnClickListener(this);
        viewMonth =createMonthView(getContext()) ;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.MATCH_PARENT);
        viewMonth.setLayoutParams(params);
        viewMonth.setOnDayClickListener(this);
        llRoot.addView(viewMonth);
    }

    public DoctorMonthView createMonthView(Context context) {
        return new DoctorMonthView(context, null, this,this);
    }

    public void setDate(int year, int monthOfYear, int dayOfMonth) {
        mCalendar.set(Calendar.YEAR, year);
        mCalendar.set(Calendar.MONTH, monthOfYear);
        mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        mDayList = createDayList(mCalendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        updataMonthView();
    }

    public void updataMonthView() {
        int selectedDay = getSelectedDay().getDay();
        final int month = getSelectedDay().getMonth();
        final int year = getSelectedDay().getYear();
        viewMonth.setMonthParams(selectedDay, year, month, getFirstDayOfWeek());
        viewMonth.invalidate();
    }

    public CalendarEntity getCalendarEntity() {
        if (mCalendarEntity == null) {
            mCalendarEntity = new CalendarEntity();
        }
        return mCalendarEntity;
    }
    public void setMonthParams(int selectedDay, int year, int month, int weekStart) {
        viewMonth.setMonthParams(selectedDay,year,month,weekStart);
    }
    /**
     * Sets an array of dates which should be highlighted when the picker is drawn
     *
     * @param highlightedDays an Array of Calendar objects containing the dates to be highlighted
     */
    @SuppressWarnings("unused")
    public void setHighlightedDays(Calendar[] highlightedDays) {
        for (Calendar highlightedDay : highlightedDays) Utils.trimToMidnight(highlightedDay);
        this.highlightedDays.addAll(Arrays.asList(highlightedDays));
    }

    @Override
    public void onYearSelected(int year) {

    }

    @Override
    public void onDayOfMonthSelected(int year, int month, int day) {
        mCalendar.set(Calendar.YEAR, year);
        mCalendar.set(Calendar.MONTH, month);
        mCalendar.set(Calendar.DAY_OF_MONTH, day);
        updataMonthView();
        updatePickers();
    }

    private void updatePickers() {
        for (OnDateChangedListener listener : mListeners) listener.onDateChanged();
    }

    @Override
    public void registerOnDateChangedListener(DatePickerDialog.OnDateChangedListener listener) {
        mListeners.add(listener);
    }

    @Override
    public void unregisterOnDateChangedListener(DatePickerDialog.OnDateChangedListener listener) {
        mListeners.remove(listener);
    }

    @Override
    public MonthAdapter.CalendarDay getSelectedDay() {
        return new MonthAdapter.CalendarDay(mCalendar, getTimeZone());
    }

    @Override
    public boolean isThemeDark() {
        return mThemeDark;
    }

    @Override
    public int getAccentColor() {
        return mAccentColor;
    }

    @Override
    public boolean isHighlighted(int year, int month, int day) {
        Calendar date = Calendar.getInstance();
        date.set(Calendar.YEAR, year);
        date.set(Calendar.MONTH, month);
        date.set(Calendar.DAY_OF_MONTH, day);
        Utils.trimToMidnight(date);
        return highlightedDays.contains(date);
    }

    @Override
    public int getFirstDayOfWeek() {
        return mWeekStart;
    }

    @Override
    public int getMinYear() {
        return mDateRangeLimiter.getMinYear();
    }

    @Override
    public int getMaxYear() {
        return mDateRangeLimiter.getMaxYear();
    }

    @Override
    public Calendar getStartDate() {
        return mDateRangeLimiter.getStartDate();
    }

    @Override
    public Calendar getEndDate() {
        return mDateRangeLimiter.getEndDate();
    }

    @Override
    public boolean isOutOfRange(int year, int month, int day) {
        return mDateRangeLimiter.isOutOfRange(year, month, day);
    }

    @Override
    public void tryVibrate() {
        if (mVibrate) mHapticFeedbackController.tryVibrate();
    }

    @Override
    public TimeZone getTimeZone() {
        return mTimezone == null ? TimeZone.getDefault() : mTimezone;
    }

    @Override
    public void onDayClick(MonthView view, MonthAdapter.CalendarDay day) {
        if (day != null) {
            onDayTapped(day);
        }
    }

    protected void onDayTapped(MonthAdapter.CalendarDay day) {
        getCalendarEntity().setYear(day.year);
        getCalendarEntity().setMonth(day.month);
        DayEntity dayEntity;
        if (mDayList.containsKey(day.day)) {
            dayEntity = mDayList.get(day.day);
            dayEntity.setSeletMode(switchDayMode(dayEntity.getSeletMode()));
        } else {
            dayEntity = new DayEntity();
            dayEntity.setSeletMode(mDateSelectMode);
        }
        dayEntity.setDay(day.day);
        tryVibrate();
        onDayOfMonthSelected(day.year, day.month, day.day);
    }

    private int switchDayMode(int mode) {
        return mode == mDateSelectMode ? DayEntity.MODE_DATE_RESET : mDateSelectMode;
    }
    private Map<Integer,DayEntity> createDayList(int maxDay) {
        Map<Integer, DayEntity> map = new HashMap<>();
        map.clear();
        int max = maxDay + 1;
        List<DayEntity> list = new ArrayList<>();
        for (int i = 1; i < max; i++) {
            DayEntity dayEntity = new DayEntity();
            dayEntity.setDay(i);
            dayEntity.setSeletMode(DayEntity.MODE_DATE_WORK);
            list.add(dayEntity);
            map.put(i, dayEntity);
        }
        return map;
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_sure:

                break;
        }
    }

    private DoctorMonthView.MonthViewEntity createMonthViewEntity(int year, int month, int day) {
        DayEntity dayEntity;
        if (mDayList.containsKey(day)) {
            dayEntity = mDayList.get(day);
        } else {
            dayEntity = new DayEntity();
            dayEntity.setDay(day);
            dayEntity.setSeletMode(mDateSelectMode);
            mDayList.put(day, dayEntity);
        }
        DoctorMonthView.MonthViewEntity monthViewEntity = getMonthViewEntity(dayEntity.getSeletMode(), day);
        return monthViewEntity;
    }

    private DoctorMonthView.MonthViewEntity getMonthViewEntity(int mode,int day) {
        String text = String.valueOf(day);
        DoctorMonthView.MonthViewEntity monthViewEntity = new DoctorMonthView.MonthViewEntity();
        switch (mode) {
            case DayEntity.MODE_DATE_RESET:
                if (mResetMonthViewEntity == null) {
                    mResetMonthViewEntity = new DoctorMonthView.MonthViewEntity();
                    mResetMonthViewEntity.isCircle = false;
                    mResetMonthViewEntity.text = String.valueOf(text);
                    mResetMonthViewEntity.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL);
                    mResetMonthViewEntity.textColor = getResources().getColor(R.color.color_text_reset);
                }
                monthViewEntity = mResetMonthViewEntity;
                break;
            case DayEntity.MODE_DATE_WORK:
                if (mWorkMonthViewEntity == null) {
                    mWorkMonthViewEntity = new DoctorMonthView.MonthViewEntity();
                    mWorkMonthViewEntity.isCircle = true;
                    mWorkMonthViewEntity.text = String.valueOf(text);
                    mWorkMonthViewEntity.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD);
                    mWorkMonthViewEntity.textColor = getResources().getColor(R.color.color_text_work);
                    mWorkMonthViewEntity.circleColor = getResources().getColor(R.color.color_circle_work);
                }
                monthViewEntity = mWorkMonthViewEntity;
                break;
            case DayEntity.MODE_DATE_ON_DUTY:
                if (mOnDutyMonthViewEntity == null) {
                    mOnDutyMonthViewEntity = new DoctorMonthView.MonthViewEntity();
                    mOnDutyMonthViewEntity.isCircle = true;
                    mOnDutyMonthViewEntity.text = String.valueOf(text);
                    mOnDutyMonthViewEntity.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD);
                    mOnDutyMonthViewEntity.textColor = getResources().getColor(R.color.color_text_on_duty);
                    mOnDutyMonthViewEntity.circleColor =getResources().getColor(R.color.color_circle_on_duty);
                }
                monthViewEntity = mOnDutyMonthViewEntity;
                break;
            case DayEntity.MODE_DATE_CUSTOM_CLOCK:
                if (mCustomClockMonthViewEntity == null) {
                    mCustomClockMonthViewEntity = new DoctorMonthView.MonthViewEntity();
                    mCustomClockMonthViewEntity.isCircle = true;
                    mCustomClockMonthViewEntity.text = String.valueOf(text);
                    mCustomClockMonthViewEntity.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD);
                    mCustomClockMonthViewEntity.textColor = getResources().getColor(R.color.color_text_custom_clock);
                    mCustomClockMonthViewEntity.circleColor = getResources().getColor(R.color.color_circle_custom_clock);
                }
                monthViewEntity = mCustomClockMonthViewEntity;
                break;
        }
        return monthViewEntity;
    }
    @Override
    public DoctorMonthView.MonthViewEntity getMonthViewEntity(int year, int month, int day) {
        return createMonthViewEntity(year,month,day);
    }
}
