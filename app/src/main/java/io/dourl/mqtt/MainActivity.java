package io.dourl.mqtt;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatCheckedTextView;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import io.dourl.mqtt.databinding.ActivityMainBinding;
import io.dourl.mqtt.manager.MqttManager;
import io.dourl.mqtt.ui.AddressFragment;
import io.dourl.mqtt.ui.BaseActivity;
import io.dourl.mqtt.ui.SessionFragment;

public class MainActivity extends BaseActivity implements View.OnClickListener {


    public static final int TO_POST = 4; // 发帖


    private int currentItem = -1;
    private int loginToPagePosition; // 登录以后要跳转的页面

    private HomeFragment mHomeFragment;
    private SocialFragment mSocialFragment;
    private SessionFragment mSessionFragment;
    private AddressFragment mAddressFragment;
    private MyFragment myFragment;
    private Fragment mContent;
    private ActivityMainBinding binding;
    private AppCompatCheckedTextView[] tabs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        initView();

        initTest();

        System.out.println("----22222----");

    }

    private void initTest() {
        System.out.println("----000----");
        if (true){
            return;
        }
        System.out.println("----11111----");
    }

    private void initView() {
        tabs = new AppCompatCheckedTextView[]{
                binding.mainTabTv0,
                binding.mainTabTv1,
                binding.mainTabTv2,
                binding.mainTabTv3,
        };

        binding.mainTabTv0.setOnClickListener(this);
        binding.mainTabTv1.setOnClickListener(this);
        binding.mainTabTv2.setOnClickListener(this);
        binding.mainTabTv3.setOnClickListener(this);
        // 默认选中
        initPage(HomeFragment.Companion.getIndex());
    }


    private void initPage(int startPage) {
        /* if (currentItem == startPage) { // 刷新等
         *//*if (currentItem == TO_CIRCLE)
               // simpleClickDetect(viewBinding.mainTabTv1);
            else if (currentItem == TO_FIRST)
                //simpleClickDetect(viewBinding.mainTabTv0);
            return;*//*
        }*/
        if (startPage == HomeFragment.Companion.getIndex()) {
            if (mHomeFragment == null)
                mHomeFragment = HomeFragment.newInstance();
            switchContent(mHomeFragment);
        } else if (startPage == SessionFragment.Companion.getTO_CIRCLE()) {
            if (mSessionFragment == null)
                mSessionFragment = SessionFragment.newInstance();
            switchContent(mSessionFragment);
        } else if (startPage == AddressFragment.Companion.getTO_ADDRESS()) {
            if (mAddressFragment == null)
                mAddressFragment = AddressFragment.newInstance();
            switchContent(mAddressFragment);
        } else if (startPage == MyFragment.Companion.getTO_ME()) {
            if (myFragment == null)
                myFragment = MyFragment.newInstance();
            switchContent(myFragment);
        }
        handlerTabView(startPage);
        currentItem = startPage;
        loginToPagePosition = startPage;
    }

    private void handlerTabView(int index) {
        for (int i = 0; i < tabs.length; i++) {
            tabs[i].setSelected(index == i);
        }
    }


    private void switchContent(Fragment to) {
        if (mContent == to)
            return;
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (!to.isAdded()) { // 先判断是否被add过
            if (mContent == null) {
                transaction.add(R.id.fl_content, to).commitAllowingStateLoss();
            } else { // 隐藏当前的fragment，add下一个到Activity中
                transaction.hide(mContent).add(R.id.fl_content, to).commitAllowingStateLoss();
            }
        } else {
            // 隐藏当前的fragment，显示下一个
            transaction.hide(mContent).show(to).commitAllowingStateLoss();
        }
        mContent = to;

       /* if (mContent instanceof UserInfoFragment) {
            StatusBarUtil.setStatusBarColor(this, R.color.msb_red_80);
        } else {
            StatusBarUtil.setStatusBarColor(this, R.color.msb_white);
        }*/
    }


    @Override
    protected void onResume() {
        super.onResume();
        MqttManager.wakeMqtt();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.main_tab_tv_0) {
            initPage(HomeFragment.Companion.getIndex());
        } else if (id == R.id.main_tab_tv_1) {
            initPage(SocialFragment.Companion.getTO_CIRCLE());
        } else if (id == R.id.main_tab_tv_2) {
            initPage(AddressFragment.Companion.getTO_ADDRESS());
        } else if (id == R.id.main_tab_tv_3) {
           /* if (!ActionUtil.isLogin(mContext, true)) {
                loginToPagePosition = TO_ME;
                return;
            }*/
           /* if (!GlobalConstants.exitUserType()) {
                Intent mIntent = new Intent(mContext, RoleActivity.class);
                mIntent.putExtra("isFromLogin", true);
                mIntent.putExtra("isRole", GlobalConstants.getUserType());
                startActivity(mIntent);
                return;
            }*/
            // Logger.log("切换到我的页面2", Log.INFO, "fkj");
            initPage(MyFragment.Companion.getTO_ME());
        } else if (id == R.id.main_post_iv) {
           /* if (TextUtils.isEmpty(GlobalConstants.getUserId())) {
                ActionUtil.isLogin(mContext, true);
                Intent i = new Intent(mContext, UserLoginActivity.class);
                loginToPagePosition = TO_POST;
            } else {
                startToPost();
            }*/
        }
    }
}