package com.wqlin.clock.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.AttributeSet;

import com.wdullaer.materialdatetimepicker.date.DatePickerController;

/**
 * @author wangql
 * @email wangql@leleyuntech.com
 * @date 2017/11/10 16:01
 */
public class DoctorMonthView extends com.wdullaer.materialdatetimepicker.date.MonthView{

    /**
     * 休息 画笔颜色
     */
    protected int mResetTextColor;
    /**
     * 正常上班
     */
    protected int mWorkTextColor;
    /**
     * 正常上班
     */
    protected int mDutyTextColor;
    protected int mCustomClockColor;

    private DateSelectController mDateSelectController;

    public DoctorMonthView(Context context) {
        super(context);
    }

    public DoctorMonthView(Context context, AttributeSet attr, DatePickerController controller, DateSelectController dateSelectController) {
        super(context, attr, controller);
        mDateSelectController = dateSelectController;
    }

    @Override
    public void drawMonthDay(Canvas canvas, int year, int month, int day, int x, int y, int startX, int stopX, int startY, int stopY) {
        if (mDateSelectController == null) {
            mMonthNumPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
            mMonthNumPaint.setColor(mDisabledDayTextColor);
            canvas.drawText(String.valueOf(day), x, y, mMonthNumPaint);
        } else {
            MonthViewEntity entity=mDateSelectController.getMonthViewEntity(year,month,day);
            mMonthNumPaint.setTypeface(entity.typeface);
            mMonthNumPaint.setColor(entity.textColor);
            String text = String.valueOf(day);
            if (TextUtils.isEmpty(entity.text)) {
                text = entity.text;
            }
            if (entity.isCircle) {
                mSelectedCirclePaint.setColor(entity.circleColor);
                canvas.drawCircle(x, y - (MINI_DAY_NUMBER_TEXT_SIZE / 3), DAY_SELECTED_CIRCLE_SIZE,mSelectedCirclePaint);
            }
            canvas.drawText(text, x, y, mMonthNumPaint);
        }
    }

    public interface DateSelectController{
        MonthViewEntity getMonthViewEntity(int year, int month, int day);
    }
    public static class MonthViewEntity{
        public boolean isCircle = false;
        public Typeface typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD);
        public String text;
        public int textColor = Color.parseColor("#ffffff");
        public int circleColor = Color.parseColor("#5CACEE");
    }
}
