package io.dourl.mqtt.ui.widge;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import io.dourl.mqtt.R;


/**
 * titlebar 控件
 * 在xml中使用的例子
 *
 *     <com.yunmall.ymsdk.widget.YmTitleBar
          xmlns:app="http://schemas.android.com/apk/res/com.yunmall.ym"
          android:layout_width="match_parent"
          android:layout_height="wrap_content"

          app:leftBgDrawable="@drawable/btn_large_mode_selector"    //左按钮 图
          app:leftPadding="5dp"                                     //左按钮 与左侧的间距

          app:rightText="找店"                                      //右按钮 图
          app:rightTextColor="#FFFFFF"                              //右按钮文本颜色
          app:rightTextSize="19"                                    //右按钮文本字体大小，值为sp
          app:rightPadding="10dp"                                   //右按钮 与右侧的间距

          app:centralText="云茂"                                       //标题文本
          app:centralTextColor="#FF0000"                               //标题文本颜色
          app:centralTextSize="20"                                     //标题字体大小

          app:tabItem1="@string/boutique"商品                         //tab1的文本
          app:tabItem2="@string/follow"                               //Tab2的文本   暂时最多支持2个tab
          app:tabItemSpace="35dp"                                     //tabitem的间距
          app:tabTextColor="@color/titlebar_tab_text_color"           //tabitem的颜色，必须是statelistcolor
          app:tabTextSize="19"/>                                      //tabitem的文本字体大小

 */
public class TitleBar extends FrameLayout {

    private TextView mLeftBtnText;
    protected TextView mRightBtnText;
    private View mLeftBtnLayout;
    private View mRightBtnLayout;
    private ImageView mLeftBtnImg;
    private View mTitleLayout;

    private ImageView mTitleImg;
    private ImageView mRightBtnImg;
    private TextView mTitleTv;
    private View mLine;

//    private RadioGroup mTabHost;
//    private RadioButton mTabItem1;
//    private RadioButton mTabItem2;
    private OnClickListener mLeftBtnClickListener = null;
    private OnClickListener mRightBtnClickListener = null;
    private OnClickListener mRightTextClickListener = null;
    private OnClickListener mTitleClickListener = null;
    private OnTitleTabChangeListener mOnTitleTabChangeListener = null;

    public TitleBar(Context context) {
        super(context);
        init(context, null);
    }

    public TitleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public TitleBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    private void init(Context mContext, AttributeSet attrs) {
        LayoutInflater.from(mContext).inflate(R.layout.widget_titlebar, this);
//        if (isInEditMode()) {
//            return;
//        }
        mLeftBtnLayout = findViewById(R.id.titlebar_left_btn_layout);
        mRightBtnLayout = findViewById(R.id.titlebar_right_btn_layout);
        mLeftBtnText = (TextView) findViewById(R.id.titlebar_left_btn);
        mLeftBtnImg = (ImageView) findViewById(R.id.titlebar_left_btn_image);
        mRightBtnText = (TextView) findViewById(R.id.titlebar_right_btn);
        mRightBtnImg = (ImageView) findViewById(R.id.titlebar_right_btn_image);
        mTitleTv = (TextView) findViewById(R.id.titlebar_title_text);
        mTitleImg = (ImageView) findViewById(R.id.titlebar_title_logo);
        mTitleLayout = findViewById(R.id.titlebar_title_layout);

//        mTabHost = (RadioGroup) findViewById(R.id.titlebar_tabhost);
//        mTabItem1 = (RadioButton) findViewById(R.id.titlebar_tabitem1);
//        mTabItem2 = (RadioButton) findViewById(R.id.titlebar_tabitem2);

        mLine = findViewById(R.id.titlebar_line);

        if (attrs != null) {
            TypedArray a = mContext.obtainStyledAttributes(attrs, R.styleable.NHTitleBar);
            init(a);
            a.recycle();
        }
    }

    private void updateBtn(View layout, TextView textView, ImageView iv, Drawable drawable, CharSequence text, ColorStateList color, float size, int padding) {
        if (drawable == null && TextUtils.isEmpty(text)) {
            layout.setVisibility(View.GONE);
        } else {
            layout.setVisibility(View.VISIBLE);
            if (!TextUtils.isEmpty(text)) {
                textView.setVisibility(View.VISIBLE);
                iv.setVisibility(View.GONE);
                textView.setBackgroundDrawable(drawable);
                textView.setText(text);
                textView.setTextColor(color);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
            } else {
                textView.setVisibility(View.GONE);
                iv.setVisibility(View.VISIBLE);
                iv.setImageDrawable(drawable);
            }

            MarginLayoutParams params = (MarginLayoutParams) layout.getLayoutParams();
            if (params != null && padding > 0) {
                params.setMargins(padding, 0, padding, 0);
            }
        }
    }
    private void updateTitleBtn(View layout, TextView textView, ImageView iv, Drawable drawable, CharSequence text, ColorStateList color, float size, int leftMargin, int rightMargin) {
        if (drawable == null && TextUtils.isEmpty(text)) {
            layout.setVisibility(View.GONE);
        } else {
            layout.setVisibility(View.VISIBLE);
            if (!TextUtils.isEmpty(text)) {
                textView.setVisibility(View.VISIBLE);
                iv.setVisibility(View.GONE);
                textView.setBackgroundDrawable(drawable);
                textView.setText(text);
                textView.setTextColor(color);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
            } else {
                textView.setVisibility(View.GONE);
                iv.setVisibility(View.VISIBLE);
                iv.setImageDrawable(drawable);
            }

            MarginLayoutParams params = (MarginLayoutParams) layout.getLayoutParams();
            if (params != null) {
                params.setMargins(leftMargin, 0,rightMargin, 0);
            }
        }
    }

    private void init(TypedArray a) {

        ColorStateList defaultColor = getResources().getColorStateList(R.color.white_color); // 默认字体的颜色 白色
        ColorStateList defaultCenterColor = getResources().getColorStateList(R.color.white_color); // 默认字体的颜色 白色
        int defaultBtnTextSize = getResources().getDimensionPixelSize(R.dimen.default_title_btn_textsize); // 默认左右按键的字体大小 17sp
        int defaultTitleTextSize = getResources().getDimensionPixelSize(R.dimen.default_title_textsize); //  默认标题字体大小 15sp
        int defaultTabItemSpace = getResources().getDimensionPixelOffset(R.dimen.default_tab_item_space); // 默认TabItem的间距为 20dp

        Drawable leftDrawable = a.getDrawable(R.styleable.NHTitleBar_leftBgDrawable);
        CharSequence leftText = a.getText(R.styleable.NHTitleBar_leftText);
        ColorStateList leftTextColor = a.getColorStateList(R.styleable.NHTitleBar_leftTextColor);
        float leftTextSize = a.getDimension(R.styleable.NHTitleBar_leftTextSize, defaultBtnTextSize);
        int leftPadding = a.getDimensionPixelSize(R.styleable.NHTitleBar_leftPadding, 0);

        Drawable rightDrawable = a.getDrawable(R.styleable.NHTitleBar_rightBgDrawable);
        CharSequence rightText = a.getText(R.styleable.NHTitleBar_rightText);
        ColorStateList rightTextColor = a.getColorStateList(R.styleable.NHTitleBar_rightTextColor);
        float rightTextSize = a.getDimension(R.styleable.NHTitleBar_rightTextSize, defaultBtnTextSize);
        int rightPadding = a.getDimensionPixelSize(R.styleable.NHTitleBar_rightPadding, 0);

        Drawable titleDrawable = a.getDrawable(R.styleable.NHTitleBar_centralBgDrawable);
        CharSequence titleText = a.getText(R.styleable.NHTitleBar_centralText);
        ColorStateList titleTextColor = a.getColorStateList(R.styleable.NHTitleBar_centralTextColor);
        int titleLeftMargin = a.getDimensionPixelSize(R.styleable.NHTitleBar_centralLeftMargin, 0);
        int titleRightMargin = a.getDimensionPixelSize(R.styleable.NHTitleBar_centralRightMargin, 0);
        float titleTextSize = a.getDimension(R.styleable.NHTitleBar_centralTextSize, defaultTitleTextSize);

//        CharSequence tabItem1 = a.getText(R.styleable.NHTitleBar_tabItem1);
//        CharSequence tabItem2 = a.getText(R.styleable.NHTitleBar_tabItem2);
//        ColorStateList colorList = a.getColorStateList(R.styleable.NHTitleBar_tabTextColor);
//        float tabTextSize = a.getDimension(R.styleable.NHTitleBar_tabTextSize, defaultTitleTextSize);
//        int tabItemSpace = a.getDimensionPixelSize(R.styleable.NHTitleBar_tabItemSpace, defaultTabItemSpace);

        if (leftTextColor == null) {
            leftTextColor = defaultColor;
        }

        if (titleTextColor == null) {
            titleTextColor = defaultCenterColor;
        }

        if (rightTextColor == null) {
            rightTextColor = defaultColor;
        }

        updateBtn(mLeftBtnLayout, mLeftBtnText, mLeftBtnImg, leftDrawable, leftText, leftTextColor, leftTextSize, leftPadding);
        updateBtn(mRightBtnLayout, mRightBtnText, mRightBtnImg, rightDrawable, rightText, rightTextColor, rightTextSize, rightPadding);
        updateTitleBtn(mTitleLayout, mTitleTv, mTitleImg, titleDrawable, titleText, titleTextColor, titleTextSize, titleLeftMargin,titleRightMargin);

//        if (!TextUtils.isEmpty(tabItem1) || !TextUtils.isEmpty(tabItem2)) {
//            mTabHost.setVisibility(View.VISIBLE);
//            mTitleLayout.setVisibility(View.VISIBLE);
//        }
//        if (!TextUtils.isEmpty(tabItem1)) {
//            mTabItem1.setVisibility(View.VISIBLE);
//            mTabItem1.setText(tabItem1);
//            mTabItem1.setTextColor(colorList);
//            mTabItem1.setTextSize(TypedValue.COMPLEX_UNIT_PX, tabTextSize);
//        }
//        if (!TextUtils.isEmpty(tabItem2)) {
//            mTabItem2.setVisibility(View.VISIBLE);
//            mTabItem2.setText(tabItem2);
//            mTabItem2.setTextColor(colorList);
//            mTabItem2.setTextSize(TypedValue.COMPLEX_UNIT_PX, tabTextSize);
//            MarginLayoutParams params = (MarginLayoutParams) mTabItem2.getLayoutParams();
//            if (params != null) {
//                params.setMargins(tabItemSpace, 0, 0, 0);
//            }
//        }

        mLeftBtnLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mLeftBtnClickListener != null) {
                    mLeftBtnClickListener.onClick(mLeftBtnLayout);
                }
            }
        });
        mRightBtnLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mRightBtnClickListener != null) {
                    mRightBtnClickListener.onClick(mRightBtnLayout);
                } else if (mRightTextClickListener != null) {
                    mRightTextClickListener.onClick(mRightBtnLayout);
                }
            }
        });
        this.setClickable(true);
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mTitleClickListener != null){
                    mTitleClickListener.onClick(TitleBar.this);
                }
            }
        });

//        mTabItem1.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (mOnTitleTabChangeListener != null) {
//                    mOnTitleTabChangeListener.onTabChanged(0);
//                }
//            }
//        });
//
//        mTabItem2.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (mOnTitleTabChangeListener != null) {
//                    mOnTitleTabChangeListener.onTabChanged(1);
//                }
//            }
//        });
    }

//    public void setTabIndex(int index) {
//        if (index == 0) {
//            mTabItem1.setChecked(true);
//        } else {
//            mTabItem2.setChecked(true);
//        }
//    }

    /**
     * 设置Title中间标题文字
     * @param title 标题文字
     */
    public void setTitle(String title) {
        if (mTitleTv != null) {
            mTitleLayout.setVisibility(View.VISIBLE);
            mTitleTv.setVisibility(View.VISIBLE);
            mTitleTv.setText(title);
        }
    }
    public TextView getmTitleTv(){
        return mTitleTv;
    }

    /**
     * 设置title中间图片
     * @param id
     */
    public void setTitleIcon(int id) {
        if (mTitleImg != null && id > 0) {
            mTitleLayout.setVisibility(View.VISIBLE);
            mTitleImg.setVisibility(View.VISIBLE);
            mTitleImg.setImageResource(id);
        }
    }

    /**
     * 设置Title标题颜色
     * @param resId
     */
    public void setTitleColor(int resId){
    	if(mTitleTv!=null){
    		ColorStateList csl=(ColorStateList)getResources().getColorStateList(resId);
    		if(csl!=null){
    		    mTitleTv.setVisibility(View.VISIBLE);
    			mTitleTv.setTextColor(csl);
    		}
    	}
    }

    /**
     * 设置Title中间标题文字
     * @param resId 标题文字
     */
    public void setTitle(int resId) {
        if (mTitleTv != null) {
            mTitleLayout.setVisibility(View.VISIBLE);
            mTitleTv.setVisibility(View.VISIBLE);
            mTitleTv.setText(resId);
        }
    }

    /**
     * 设置左按键图标
     * @param drawable
     */
    public void setLeftDrawable(int drawable){
        setLeftImgVisiable();
        mLeftBtnImg.setImageResource(drawable);
    }

    private void setLeftImgVisiable() {
        setLeftVisiable(View.VISIBLE);
        mLeftBtnImg.setVisibility(View.VISIBLE);
        mLeftBtnText.setVisibility(View.GONE);
    }

    /**
     * 设置左按键文本
     * @param resId
     */
    public void setLeftText(int resId){
        mLeftBtnText.setText(resId);
        setLeftTextVisiable();
    }

    /**
     * 设置左按键文本
     * @param str
     */
    public void setLeftText(String str){
        mLeftBtnText.setText(str);
        setLeftTextVisiable();
    }


    private void setLeftTextVisiable() {
//        setLeftVisiable(View.VISIBLE);
        mLeftBtnText.setVisibility(View.VISIBLE);
        mLeftBtnImg.setVisibility(View.GONE);
    }

    /**
     * 设置左按键字体颜色
     * @param resId
     */
    public void setLeftTextColor(int resId){
        if(mLeftBtnText!=null){
            ColorStateList csl=(ColorStateList)getResources().getColorStateList(resId);
            if(csl!=null){
                mLeftBtnText.setVisibility(View.VISIBLE);
                mLeftBtnText.setTextColor(csl);
            }
        }
    }

    /**
     * 设置右按键字体标题颜色
     * @param resId color资源id
     */
    public void setRightTextColor(int resId){
        if(mRightBtnText!=null){
            ColorStateList csl= getResources().getColorStateList(resId);
            if(csl!=null){
                mRightBtnText.setVisibility(View.VISIBLE);
                mRightBtnText.setTextColor(csl);
            }
        }
    }

    /**
     * 设置左按键可见性
     * @param visible
     */
    public void setLeftVisiable(int visible){
        mLeftBtnLayout.setVisibility(visible);
    }

    /**
     * 设置右按键图标
     * @param drawable
     */
    public void setRightDrawable(int drawable) {
        if (drawable > 0) {
            setRightImgVisiable();
            mRightBtnImg.setImageResource(drawable);
        } else {
            mRightBtnImg.setVisibility(View.GONE);
        }
    }

    private void setRightImgVisiable() {
        setRightVisiable(View.VISIBLE);
        mRightBtnImg.setVisibility(View.VISIBLE);
        mRightBtnText.setVisibility(View.GONE);
    }

    /**
     * 设置右按键文本
     * @param resId
     */
    public void setRightText(int resId){
        mRightBtnText.setText(resId);
        setRightTextVisiable();
    }

    /**
     * 设置右按键文本
     * @param str
     */
    public void setRightText(String str){
        mRightBtnText.setText(str);
        setRightTextVisiable();
    }

    private void setRightTextVisiable() {
        setRightVisiable(View.VISIBLE);
        mRightBtnText.setVisibility(View.VISIBLE);
        mRightBtnImg.setVisibility(View.GONE);
    }

    /**
     * 设置左按键间距
     * @param left   dip值
     * @param top  dip值
     * @param right  dip值
     * @param bottom  dip值
     */
    public void setLeftMargins(float left, float top, float right, float bottom){
        float density = getResources().getDisplayMetrics().density;
        int marginLeft = (int)(left * density);
        int marginTop = (int)(top * density);
        int marginRight = (int)(right * density);
        int marginBottom = (int)(bottom * density);
        MarginLayoutParams params = (MarginLayoutParams) mLeftBtnLayout.getLayoutParams();
        if(params != null){
            params.setMargins(marginLeft, marginTop, marginRight, marginBottom);
        }
    }

    /**
     * 设置右按键间距
     * @param left   dip值
     * @param top  dip值
     * @param right  dip值
     * @param bottom  dip值
     */
    public void setRightMargins(float left, float top, float right, float bottom){
        float density = getResources().getDisplayMetrics().density;
        int marginLeft = (int)(left * density);
        int marginTop = (int)(top * density);
        int marginRight = (int)(right * density);
        int marginBottom = (int)(bottom * density);
        MarginLayoutParams params = (MarginLayoutParams) mRightBtnText.getLayoutParams();
        if(params != null){
            params.setMargins(marginLeft, marginTop, marginRight, marginBottom);
        }
    }

    /**
     * 设置右按键可见性
     * @param visible
     */
    public void setRightVisiable(int visible){
        mRightBtnLayout.setVisibility(visible);
    }

    public void setRightEnable(boolean enable) {
        mRightBtnLayout.setEnabled(enable);
        mRightBtnImg.setEnabled(enable);
        mRightBtnText.setEnabled(enable);
    }

    public void setLeftEnable(boolean enable) {
        mLeftBtnLayout.setEnabled(enable);
        mLeftBtnImg.setEnabled(enable);
        mLeftBtnText.setEnabled(enable);
    }

    public void showLine() {
        mLine.setVisibility(View.VISIBLE);
    }

    public void HideLine() {
        mLine.setVisibility(View.GONE);
    }

    public View getRightButton() {
        return mRightBtnLayout;

    }
    public View getLeftButton() {
        return mLeftBtnLayout;

    }

    public void setRightTextListener(OnClickListener mListener) {
        mRightTextClickListener = mListener;
    }

    public interface SpaceOnClickListener {
        public void OnClick();
    }
    /**
     * 设置左按钮按键响应
     * @param mListener
     */
    public void setLeftBtnListener(OnClickListener mListener) {
        mLeftBtnClickListener = mListener;
    }

    /**
     * 设置右按钮按键响应
     * @param mListener
     */
    public void setRightBtnListener(OnClickListener mListener) {
        mRightBtnClickListener = mListener;
    }

    /**
     * 设置title非按钮区域按键响应
     * @param mListener
     */
    public void setTitleListener(OnClickListener mListener) {
        mTitleClickListener = mListener;
    }

    /**
     * 设置tab的响应
     * @param mListener
     */
    public void setOnTitleTabChangeListener(OnTitleTabChangeListener mListener) {
        mOnTitleTabChangeListener = mListener;
    }

    /**
     * tabitem切换监听器
     */
    public interface OnTitleTabChangeListener {
        public void onTabChanged(int index);
    }

}
