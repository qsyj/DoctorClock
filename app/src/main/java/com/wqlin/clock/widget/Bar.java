package com.wqlin.clock.widget;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.wqlin.clock.R;

/**
 * TODO
 *
 * @author Wang BinLin
 * @Date 16/11/15
 * @since 1.0.0
 */

public class Bar {
    private Toolbar mToolbar;
    private TextView mToolbarTitle;
    private TextView mToolbarBack;
    private TextView mToolbarNext;
    int homeId = Menu.FIRST;
    private View.OnClickListener barRight1OnClickListener;
    private View.OnClickListener barRight2OnClickListener;

    public Bar(View view) {
        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
        mToolbarTitle = (TextView) view.findViewById(R.id.toolbar_title);
        mToolbarBack = (TextView) view.findViewById(R.id.toolbar_back);
        mToolbarNext = (TextView) view.findViewById(R.id.toolbar_next);
        mToolbar.setTitle("");
//        mToolbar.setNavigationIcon(R.drawable.ic_back_default);
    }

    public Bar(AppCompatActivity activity) {
        this(activity.findViewById(android.R.id.content));
        activity.setSupportActionBar(getToolbar());
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public Toolbar getToolbar() {
        return mToolbar;
    }

    public TextView getToolbarTitle() {
        return mToolbarTitle;
    }

    public TextView getmToolbarBack() {
        return mToolbarBack;
    }

    public TextView getmToolbarNext() {
        return mToolbarNext;
    }

    public void setToolbarColor(@ColorInt int color) {
        getToolbar().setBackgroundColor(color);
    }

    public void setTitle(String title) {
        getToolbarTitle().setText(title);
    }

    public void setTitle(String title, @ColorInt int color) {
        getToolbarTitle().setText(title);
        getToolbarTitle().setTextColor(color);
    }

    public void setNextText(String nextText, View.OnClickListener listener) {
        setNextText(nextText, 0, listener);
    }

    public void setNextText(String nextText, int color, View.OnClickListener listener) {
        getmToolbarNext().setText(nextText);
        getmToolbarNext().setVisibility(View.VISIBLE);
        getmToolbarNext().setOnClickListener(listener);
    }

    public void setNavigationIcon(@DrawableRes int resId) {
        getToolbar().setNavigationIcon(resId);
    }

    public void setNavigationText(String text, View.OnClickListener listener) {
        setNavigationText(text, 0, listener);
    }

    public void setNavigationText(String text, @ColorInt int color, View.OnClickListener listener) {
        getmToolbarBack().setText(text);
        getmToolbarBack().setVisibility(View.VISIBLE);
        getmToolbarBack().setOnClickListener(listener);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void setNavigationTextDrawable(String text, @ColorInt int color, Drawable drawableLeft, View.OnClickListener listener) {
        getmToolbarBack().setCompoundDrawablesRelativeWithIntrinsicBounds(drawableLeft, null, null, null);
        setNavigationText(text, color, listener);
    }

    public void setNavigationOnClickListener(View.OnClickListener listener) {
        getToolbar().setNavigationOnClickListener(listener);
    }

    public void setMenu(final String menuName, final View.OnClickListener onClickListener) {

        getToolbar().post(new Runnable() {
            @Override
            public void run() {
                getMenu().add(homeId, R.id.bar_right_1, Menu.NONE, menuName)
                        .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
                barRight1OnClickListener = onClickListener;
                mToolbar.setOnMenuItemClickListener(onMenuItemClickListener);

            }
        });
    }


    public void setMenu(@DrawableRes final int iconRes, final View.OnClickListener onClickListener) {
        getToolbar().post(new Runnable() {
            @Override
            public void run() {
                getMenu().add(homeId, R.id.bar_right_1, Menu.NONE, "")
                        .setIcon(iconRes)
                        .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
                barRight1OnClickListener = onClickListener;
                mToolbar.setOnMenuItemClickListener(onMenuItemClickListener);

            }
        });
    }
    public void setMenu(final Drawable iconDrawable, final View.OnClickListener onClickListener) {
        getToolbar().post(new Runnable() {
            @Override
            public void run() {
                getMenu().add(homeId, R.id.bar_right_1, Menu.NONE, "")
                        .setIcon(iconDrawable)
                        .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
                barRight1OnClickListener = onClickListener;
                mToolbar.setOnMenuItemClickListener(onMenuItemClickListener);

            }
        });
    }

    private Menu getMenu() {
        return getToolbar().getMenu();
    }

    private Toolbar.OnMenuItemClickListener onMenuItemClickListener = new Toolbar.OnMenuItemClickListener() {

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            if (item.getItemId() == R.id.bar_right_1) {
                if (barRight1OnClickListener != null) {
                    barRight1OnClickListener.onClick(item.getActionView());
                }
            } else if (item.getItemId() == R.id.bar_right_2) {
                if (barRight2OnClickListener != null) {
                    barRight2OnClickListener.onClick(item.getActionView());
                }
            } else {

            }
            return true;
        }
    };
}
