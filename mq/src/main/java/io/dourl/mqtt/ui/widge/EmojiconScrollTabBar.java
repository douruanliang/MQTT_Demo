package io.dourl.mqtt.ui.widge;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import androidx.core.view.ViewCompat;



import java.util.ArrayList;
import java.util.List;

import io.dourl.mqtt.R;
import io.dourl.mqtt.utils.AppContextUtil;

public class EmojiconScrollTabBar extends RelativeLayout{

    private Context context;
    private HorizontalScrollView scrollView;
    private LinearLayout tabContainer;

    private List<ImageView> tabList = new ArrayList<ImageView>();
    private ScrollTabBarItemClickListener itemClickListener;

    public EmojiconScrollTabBar(Context context) {
        this(context, null);
    }

    public EmojiconScrollTabBar(Context context, AttributeSet attrs, int defStyle) {
        this(context, attrs);
    }

    public EmojiconScrollTabBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }
    
    private void init(Context context, AttributeSet attrs){
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.im_widget_emojicon_tab_bar, this);
        
        scrollView = findViewById(R.id.scroll_view);
        tabContainer = findViewById(R.id.tab_container);
    }
    
    /**
     * add tab
     * @param icon
     */
    public void addTab(int icon){
        View tabView = View.inflate(context, R.layout.im_scroll_tab_item, null);
        ImageView imageView = tabView.findViewById(R.id.iv_icon);
        imageView.setImageResource(icon);
        int tabWidth = 60;
        LinearLayout.LayoutParams imgParams = new LinearLayout.LayoutParams(AppContextUtil.dip2px(tabWidth), LayoutParams.MATCH_PARENT);
        imageView.setLayoutParams(imgParams);
        tabContainer.addView(tabView);
        tabList.add(imageView);
        final int position = tabList.size() -1;
        imageView.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                if(itemClickListener != null){
                    itemClickListener.onItemClick(position);
                }
            }
        });
    }
    
    /**
     * remove tab
     * @param position
     */
    public void removeTab(int position){
        tabContainer.removeViewAt(position);
        tabList.remove(position);
    }
    
    public void selectedTo(int position){
        scrollTo(position);
        for (int i = 0; i < tabList.size(); i++) {
            if (position == i) {
                tabList.get(i).setBackgroundColor(getResources().getColor(R.color.emojicon_tab_selected));
            } else {
                tabList.get(i).setBackgroundColor(getResources().getColor(R.color.emojicon_tab_nomal));
            }
        }
    }
    
    private void scrollTo(final int position){
        int childCount = tabContainer.getChildCount();
        if(position < childCount){
            scrollView.post(new Runnable() {
                @Override
                public void run() {
                    int mScrollX = tabContainer.getScrollX();
                    int childX = (int) ViewCompat.getX(tabContainer.getChildAt(position));

                    if(childX < mScrollX){
                        scrollView.scrollTo(childX,0);
                        return;
                    }

                    int childWidth = tabContainer.getChildAt(position).getWidth();
                    int hsvWidth = scrollView.getWidth();
                    int childRight = childX + childWidth;
                    int scrollRight = mScrollX + hsvWidth;

                    if(childRight > scrollRight){
                        scrollView.scrollTo(childRight - scrollRight,0);
                        return;
                    }
                }
            });
        }
    }
    
    
    public void setTabBarItemClickListener(ScrollTabBarItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }
    
    
    public interface ScrollTabBarItemClickListener{
        void onItemClick(int position);
    }

}
