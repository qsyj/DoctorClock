package com.wqlin.clock.utils;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.alibaba.fastjson.JSONObject;
import com.wqlin.clock.entity.CalendarEntity;
import com.wqlin.clock.entity.ClockTimeEntity;
import com.wqlin.clock.entity.DayEntity;
import com.wqlin.clock.entity.TimeEntity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * @author wangql
 * @email wangql@leleyuntech.com
 * @date 2017/11/14 9:44
 */
public class AppUtils {
    public static final String DEFAULT_SELECT_MODE_TEXT = "工作";
    public static final String TEXT_WORK = "工作";
    public static final String TEXT_ON_DUTY = "值班";
    public static final String TEXT_RESET = "休息";
    public static final int DEFAULT_SELECT_MODE = DayEntity.MODE_DATE_WORK;
    public static long getTime(int hourOfDay, int minute, int second) {
        return hourOfDay * 60L * 60L + minute * 60L + second * 1L;
    }

    public static void gotoHome(Context context) {
        Intent home = new Intent(Intent.ACTION_MAIN);
        home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        home.addCategory(Intent.CATEGORY_HOME);
        context.startActivity(home);
    }
    public static TimeEntity createTimeEntity(int mode, int hourOfDay, int minute, int second) {
        TimeEntity timeEntity = new TimeEntity();
        timeEntity.setSeletMode(mode);
        timeEntity.setHour(hourOfDay);
        timeEntity.setMinute(minute);
        timeEntity.setSecond(second);
        return timeEntity;
    }

    public static boolean containTime(long time, List<TimeEntity> timeEntities) {
        if (timeEntities==null)
            return false;
        for (TimeEntity timeEntity: timeEntities) {
            long time1 = AppUtils.getTime(timeEntity.getHour(),timeEntity.getMinute(),timeEntity.getSecond());
            if (time1==time)
                return true;
        }
        return false;
    }

    public static String getClockTimeKey() {
        return "clock_time";
    }

    public static String getCalendarKey(int year, int month) {
        return year + "_" + month;
    }

    public static String getCalendarJsonStr(Context context,int year, int month) {
        return PrefUtils.getString(context, AppUtils.getCalendarKey(year, month));
    }

    public static CalendarEntity getCalendarEntity(Context context,int year, int month) {
        String jsonStr=getCalendarJsonStr(context, year, month);
        if (!TextUtils.isEmpty(jsonStr)) {
            CalendarEntity calendarEntity= JSONObject.parseObject(jsonStr, CalendarEntity.class);
            if (calendarEntity != null&&calendarEntity.getYear()==year&&calendarEntity.getMonth()==month) {
                return calendarEntity;
            }
        }
        return null;
    }

    public static void putCalendarEntity(Context context, CalendarEntity entity,int year, int month) {
        if (entity==null||context==null)
            return;
        String json=JSONObject.toJSONString(entity);
        String key = AppUtils.getCalendarKey(year, month);
        PrefUtils.putString(context, key, json);
        startClock(context);
    }

    public static List<ClockTimeEntity> getClockTimeList(Context context) {
        List<ClockTimeEntity> clockTimeList = null;
        String jsonStr = PrefUtils.getString(context, AppUtils.getClockTimeKey());
        if (!TextUtils.isEmpty(jsonStr)) {
            clockTimeList= JSONObject.parseArray(jsonStr, ClockTimeEntity.class);
        }
        if (clockTimeList == null) {
            clockTimeList = new ArrayList<>();
        }
        return clockTimeList;
    }

    public static void putClockTimeList(Context context,List<ClockTimeEntity> list) {
        String jsonStr=JSONObject.toJSONString(list);
        PrefUtils.putString(context, AppUtils.getClockTimeKey(), jsonStr);
        startClock(context);
    }
    public static String getSelectModeText(int seletMode) {
        String text = DEFAULT_SELECT_MODE_TEXT;
        switch (seletMode) {
            case DayEntity.MODE_DATE_RESET:
                text = TEXT_RESET;
                break;
            case DayEntity.MODE_DATE_WORK:
                text = TEXT_WORK;
                break;
            case DayEntity.MODE_DATE_ON_DUTY:
                text = TEXT_ON_DUTY;
                break;
            case DayEntity.MODE_DATE_CUSTOM_CLOCK:
                text = TEXT_WORK;
                break;
        }
        return text;
    }

    public static String getTimeFormat(TimeEntity entity) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,entity.getHour());
        calendar.set(Calendar.MINUTE,entity.getMinute());
        calendar.set(Calendar.SECOND,entity.getSecond());
        return StringUtils.friendTime2(calendar.getTime());
    }

    public static void startClock(Context context) {
        Calendar calendar=Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        CalendarEntity calendarEntity = AppUtils.getCalendarEntity(context, year, month);
        if (calendarEntity != null) {
            List<ClockTimeEntity> clockTimeList = AppUtils.getClockTimeList(context);
            int clockTimeListSize = clockTimeList.size();
            if (clockTimeListSize>0) {
                ClockTimeEntity resetEntity = null;
                ClockTimeEntity workEntity = null;
                ClockTimeEntity onDutyEntity = null;
                for (ClockTimeEntity entity:clockTimeList) {
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
                List<DayEntity> dayList = calendarEntity.getDayList();
                AlarmManagerUtil.cancel(context);
                for (DayEntity dayEntity:dayList) {
                    switch (dayEntity.getSeletMode()) {
                        case DayEntity.MODE_DATE_RESET:
                            if (startClock(context,year,month,dayEntity,resetEntity))
                                return;
                            break;
                        case DayEntity.MODE_DATE_WORK:
                            if (startClock(context,year,month,dayEntity,workEntity))
                                return;
                            break;
                        case DayEntity.MODE_DATE_ON_DUTY:
                            if (startClock(context,year,month,dayEntity,onDutyEntity))
                                return;
                            break;
                    }
                }
            }

        }
    }

    public static boolean startClock(Context context,int year,int month,DayEntity dayEntity,ClockTimeEntity clockTimeEntity) {
        boolean isStart = false;
        if (clockTimeEntity==null)
            return isStart;
        if (dayEntity==null)
            return isStart;
        List<TimeEntity> timeList=clockTimeEntity.getTimeList();
        if (timeList==null)
            return isStart;
        int size = timeList.size();
        if (size==0)
            return isStart;
        int mode1 = clockTimeEntity.getSeletMode();
        int mode2 = dayEntity.getSeletMode();
        if (mode1!=mode2)
            return isStart;
        for (TimeEntity timeEntity : timeList) {
            int day = dayEntity.getDay();
            int hour = timeEntity.getHour();
            int minute = timeEntity.getMinute();
            int second = timeEntity.getSecond();
            if (!isBeforeTime(year, month, day, hour, minute, second)) {
                Calendar calendar=Calendar.getInstance();
                calendar.set(year, month, day, hour, minute, 1);
                AlarmManagerUtil.setAlarm(context,0,calendar.getTimeInMillis(),"汪哥叫你起床了",2);
                return  true;
            }
        }
        return isStart;
    }

    public static boolean isBeforeTime(int year,int month,int day,int hour,int minute,int second) {
        Calendar calendar=Calendar.getInstance();
        long currentTime = calendar.getTimeInMillis();
        calendar.set(Calendar.YEAR,year);
        calendar.set(Calendar.MONTH,month);
        calendar.set(Calendar.DAY_OF_MONTH,day);
        calendar.set(Calendar.HOUR_OF_DAY,hour);
        calendar.set(Calendar.MINUTE,minute);
        calendar.set(Calendar.SECOND, second);
        long time = calendar.getTimeInMillis();
        if (time<=currentTime)
            return true;
        return false;
    }
}
