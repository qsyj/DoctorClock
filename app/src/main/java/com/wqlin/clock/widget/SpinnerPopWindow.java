package com.wqlin.clock.widget;

import android.app.Activity;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.wqlin.clock.R;
import com.wqlin.clock.utils.DensityUtil;
import com.wqlin.clock.widget.basepopup.BasePopupWindow;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wangql
 * @email wangql@leleyuntech.com
 * @date 2017/9/13 15:46
 */
public class SpinnerPopWindow extends BasePopupWindow {
    private ItemSelectListener mItemSelectListener;
    private List<ItemInfo> mItemList = new ArrayList<>();
    private ScrollView mContentView;
    private LinearLayout mRootView;
    /**
     * dp
     */
    private final int DEFAULT_MIN_WIDTH = 120;
    /**
     * dp
     */
    private final int DEFAULT_PADDING_LEFT = 14;
    /**
     * dp
     */
    private final int DEFAULT_PADDING_RIGHT = 14;
    /**
     * dp
     */
    private final int DEFAULT_ITEM_PADDING_TOP = 18;
    /**
     * dp
     */
    private final int DEFAULT_ITEM_PADDING_BOTTOM =18;
    private int contentViewBackGroudColor = Color.parseColor("#f6f6f6");
    private  int minWidth;
    private  int paddingLeft;
    private  int paddingRight;
    /**
     * 当没有设置item的高度时 需要设置item paddingtop  paddingBottom
     */
    private int itemPaddingTop;
    /**
     * 当没有设置item的高度时 需要设置item paddingtop  paddingBottom
     */
    private int itemPaddingBottom;
    public SpinnerPopWindow(Activity context,@NonNull ArrayList<String> textList) {
        super(context, ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        init(getItemList(textList));
    }
    public SpinnerPopWindow(Activity context,@NonNull List<ItemInfo> itemInfoList) {
        super(context, ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);

        init(itemInfoList);
    }

    @Override
    public View onCreatePopupView() {
        mContentView =new ScrollView(getContext());
        mContentView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        return mContentView;
    }
    private void init( @NonNull List<ItemInfo> itemList) {
        setItemList(itemList);
        setMinWidth(DensityUtil.dp2px(getContext(),DEFAULT_MIN_WIDTH));
        setPaddingLeft(DensityUtil.dp2px(getContext(),DEFAULT_PADDING_LEFT));
        setPaddingRight(DensityUtil.dp2px(getContext(),DEFAULT_PADDING_RIGHT));
        setItemPaddingTop(DensityUtil.dp2px(getContext(),DEFAULT_ITEM_PADDING_TOP));
        setItemPaddingBottom(DensityUtil.dp2px(getContext(),DEFAULT_ITEM_PADDING_BOTTOM));
        setItemList(itemList);

        mContentView.setBackgroundColor(contentViewBackGroudColor);
        mRootView = new LinearLayout(getContext());

        mRootView.setOrientation(LinearLayout.VERTICAL);
        mRootView.setPadding(getPaddingLeft(),0,getPaddingRight(),0);
        mContentView.addView(mRootView,new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        initRootView();
        mRootView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int rootViewMeasuredWidth = mRootView.getMeasuredWidth();
        if (rootViewMeasuredWidth < getMinWidth()) {
            ViewGroup.LayoutParams rootViewP = mRootView.getLayoutParams();
            int childCount = mRootView.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View childView = mRootView.getChildAt(i);
                ViewGroup.LayoutParams childP = childView.getLayoutParams();
                childP.width = getMinWidth();
                childView.setLayoutParams(childP);
            }
        }
    }

    private ViewGroup creatContentLayout(ItemInfo itemInfo) {
        LinearLayout contentLayout = new LinearLayout(getContext());
        contentLayout.setOrientation(LinearLayout.HORIZONTAL);
        int itemtHeight=itemInfo.getHeight();
        int contentLayoutPH = itemtHeight;
        if (itemtHeight <= 0) {
            contentLayoutPH= ViewGroup.LayoutParams.WRAP_CONTENT;
            contentLayout.setPadding(0,getItemPaddingTop(),0,getItemPaddingBottom());
        }

        RelativeLayout.LayoutParams contentLayoutP = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, contentLayoutPH);
        contentLayout.setLayoutParams(contentLayoutP);
        contentLayout.setGravity(Gravity.CENTER_VERTICAL);

        TextView tv = new TextView(getContext());
        tv.setTextColor(itemInfo.textColor);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP,itemInfo.textSize);
        LinearLayout.LayoutParams tvP = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        tv.setLayoutParams(tvP);
        contentLayout.addView(tv);
        bindData(itemInfo,contentLayout,tv);
        return contentLayout;
    }
    private void initRootView() {
        mRootView.removeAllViews();
        mRootView.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
        mRootView.setDividerDrawable(getContext().getResources().getDrawable(R.drawable.divider_ver_1dp));
        int size= mItemList.size();
        for (int i = 0; i < size; i++) {
            ItemInfo itemInfo = mItemList.get(i);
            mRootView.addView(creatContentLayout(itemInfo));
        }
    }
    /**
     * 显示数据  设置Item监听
     * @param info
     * @param tv
     */
    private void bindData(final ItemInfo info,ViewGroup contentLayout,TextView tv) {

        tv.setText(info.text);
        contentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mItemSelectListener != null) {
                    mItemSelectListener.onItemClick(info);
                }
                dismiss();
            }
        });

    }

    public void setItemSelectListener(ItemSelectListener itemSelectListener) {
        mItemSelectListener = itemSelectListener;
    }

    private List<ItemInfo> getItemList(@NonNull ArrayList<String> textList) {
        List<ItemInfo> itemInfoList = new ArrayList<>();
        int size = textList.size();
        for (int i = 0; i < size; i++) {
            itemInfoList.add(new ItemInfo(textList.get(i),i));
        }
        return itemInfoList;
    }

    private void setItemList(@NonNull List<ItemInfo> itemList) {
        this.mItemList = itemList;
    }
    @Override
    public View initAnimaView() {
        return null;
    }

    @Override
    protected Animation initShowAnimation() {
        return null;
    }

    @Override
    public View getClickToDismissView() {
        return null;
    }

    public int getMinWidth() {
        return minWidth;
    }

    public void setMinWidth(int minWidth) {
        this.minWidth = minWidth;
    }

    public int getPaddingLeft() {
        return paddingLeft;
    }

    public void setPaddingLeft(int paddingLeft) {
        this.paddingLeft = paddingLeft;
    }

    public int getPaddingRight() {
        return paddingRight;
    }

    public void setPaddingRight(int paddingRight) {
        this.paddingRight = paddingRight;
    }

    public int getItemPaddingTop() {
        return itemPaddingTop;
    }

    public void setItemPaddingTop(int itemPaddingTop) {
        this.itemPaddingTop = itemPaddingTop;
    }

    public int getItemPaddingBottom() {
        return itemPaddingBottom;
    }

    public void setItemPaddingBottom(int itemPaddingBottom) {
        this.itemPaddingBottom = itemPaddingBottom;
    }

    public static class ItemInfo{
        private String text;
        private int textColor= Color.parseColor("#000000");
        /**
         * sp
         */
        private int textSize = 14;
        private int position;
        private int resId=-1;
        /**
         * px
         */
        private int height=0;
        private Object tag;

        public ItemInfo(String text,int position) {
            this.text = text;
            this.position = position;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public int getResId() {
            return resId;
        }

        public void setResId(int resId) {
            this.resId = resId;
        }

        public int getTextColor() {
            return textColor;
        }

        public void setTextColor(int textColor) {
            this.textColor = textColor;
        }

        /**
         * sp
         * @return
         */
        public int getTextSize() {
            return textSize;
        }

        /**
         * sp
         */
        public void setTextSize(int textSize) {
            this.textSize = textSize;
        }

        public int getPosition() {
            return position;
        }

        public void setPosition(int position) {
            this.position = position;
        }

        /**
         * px
         * @return
         */
        public int getHeight() {
            return height;
        }

        /**
         * px
         * @param height
         */
        public void setHeight(int height) {
            this.height = height;
        }

        public Object getTag() {
            return tag;
        }

        public void setTag(Object tag) {
            this.tag = tag;
        }
    }
    //自定义list中item的点击监听
    public interface ItemSelectListener {
        void onItemClick(ItemInfo itemInfo);
    }
}
