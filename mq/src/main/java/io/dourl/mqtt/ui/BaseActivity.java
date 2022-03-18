package io.dourl.mqtt.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.accessibility.AccessibilityNodeInfo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

/**
 * @author: douruanliang
 * @date: 2022/3/6
 */
public class BaseActivity extends AppCompatActivity {


    //protected ProgressDialog mProgressDialog;

    protected Fragment mFragment;

    protected boolean mCustomStatusBar = false;
    private View mNoNetView;
    protected View emptyView;

    public static Handler sHandler = new Handler(Looper.getMainLooper());

    public static void runOnUi(Runnable runnable) {
        sHandler.post(runnable);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Language.setFromPreference(this);
       /* if (EmulatorKillerKt.isEmulator() || EmulatorKillerKt.getXposed()) {
            finish();
            return;
        }*/
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        if (!mCustomStatusBar) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                ViewGroup mContentView = (ViewGroup) findViewById(Window.ID_ANDROID_CONTENT);
                mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                //StatusBarCompat.setStatusBarColor(this, getResources().getColor(R.color.transparent));
            } else {
                //StatusBarCompat.setStatusBarColor(this, getResources().getColor(R.color.statusbar_color));
            }
        }
        antiAutoOnClick();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //BugTagsUtils.onResume(this);
        //AnalyticsUtils.onPageStart(this, this.getClass().getSimpleName());
        checkForceAuth();
    }

    @Override
    protected void onPause() {
        super.onPause();
        hideLoadingProgress();
        //BugTagsUtils.onPause(this);
        //AnalyticsUtils.onPageEnd(this, this.getClass().getSimpleName());
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        // BugTagsUtils.onDispatchTouchEvent(this, ev);
        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        NHLog.tag(this.getClass().getSimpleName()).v("onDestroy");
    }

    /**
     * 子类需要自定义通知栏颜色，
     * 则应该在setContentView前调用该方法
     */
    protected void useCustomStatusBar() {
        mCustomStatusBar = true;
    }

    protected void addFragment(int container, Fragment fragment) {
        mFragment = fragment;
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        // ft.setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out);
        ft.add(container, fragment);
//        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.addToBackStack(null);
        ft.commitAllowingStateLoss();
    }

    protected void changeFragment(int container, Fragment fragment) {
        if (isFinishing()) {
            return;
        }
        mFragment = fragment;
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        //ft.setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out);
        ft.replace(container, fragment);
//        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.addToBackStack(null);
        ft.commitAllowingStateLoss();
    }

    protected void removeFragment(Fragment fragment) {
        mFragment = fragment;
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        //ft.setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out);
        ft.remove(fragment);
//        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
        ft.addToBackStack(null);
        ft.commitAllowingStateLoss();
    }

    protected boolean popFragment() {
        return getFragmentManager().popBackStackImmediate();
    }

    public void showLoadingProgress(String str) {
        showLoadingProgress(str, true, null);
    }

    public void showLoadingProgress(final String str, final boolean cancelable,
                                    final DialogInterface.OnCancelListener listener) {
       /* if (this.isFinishing())
            return;
        runOnUiThread(() -> {
            if (getLifecycle().getCurrentState() == Lifecycle.State.DESTROYED) {
                return;
            }
            if (mProgressDialog != null && mProgressDialog.isShowing()) {
                return;
            }
            if (mProgressDialog == null) {
                mProgressDialog = new ProgressDialog(BaseActivity.this, str);
            }
            mProgressDialog.setCancelable(cancelable);
            if (listener != null) {
                mProgressDialog.setOnCancelListener(listener);
            }
            try {
                mProgressDialog.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });*/
    }

    public void showLoadingProgress() {
        showLoadingProgress("");
    }

    public void showLoadingProgress(boolean cancelable) {
        /*showLoadingProgress(getString(R.string.loading), cancelable, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {

            }
        });*/
    }

    public void hideLoadingProgress() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                  /*  if (mProgressDialog != null && mProgressDialog.isShowing()) {
                        mProgressDialog.dismiss();
                    }*/
                } catch (Exception e) {
                }
            }
        });
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        //overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    @Override
    public void finish() {
        super.finish();
        //overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    /**
     * 权限被拒绝后的提示信息
     */
    public void ShowPermissionDeniedToast() {
        // NHToastUtils.showLongToast(R.string.permission_error_unable_to_use_feature);
    }

    public void showNoNetView() {
        // showNoNetView(R.dimen.base_no_internet);
    }

    public void hideNoNetView() {
        if (null != mNoNetView) {
            mNoNetView.setVisibility(View.GONE);
        }
    }

    public void showNoNetView(int dimenId) {
        if (null == mNoNetView) {
            int topMargin;
            try {
                topMargin = getResources().getDimensionPixelSize(dimenId);
            } catch (Exception e) {
                e.printStackTrace();
                topMargin = 225;
            }
           /* mNoNetView = getLayoutInflater().inflate(R.layout.activity_base_no_net, null);
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.topMargin = topMargin;
            addContentView(mNoNetView, params);
            mNoNetView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mNoNetView.setVisibility(View.GONE);
                    reload();
                }
            });*/
        } else {
            mNoNetView.setVisibility(View.VISIBLE);
        }
    }

    protected void reload() {
        showLoadingProgress();
    }

    public void showEmptyView(int drawableId, int textId) {
        /*if (emptyView == null) {
            emptyView = getLayoutInflater().inflate(R.layout.layout_empty_view, null);
            TextView textView = (TextView) emptyView.findViewById(R.id.empty_view);
            if (textId > 0) {
                textView.setText(textId);
            }
            FrameLayout.LayoutParams params = null;
            if (drawableId > 0) {
                textView.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(drawableId), null, null);
                params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.topMargin = getResources().getDimensionPixelSize(R.dimen.empty_view_top_margin);
            }
            addContentView(emptyView, params);
        } else {
            emptyView.setVisibility(View.VISIBLE);
        }*/
    }

    public void hideEmptyView() {
        if (emptyView != null) {
            emptyView.setVisibility(View.GONE);
        }
    }

    private void antiAutoOnClick() {
        ViewGroup contentView = findViewById(Window.ID_ANDROID_CONTENT);
        contentView.setAccessibilityDelegate(new View.AccessibilityDelegate() {
            @Override
            public boolean performAccessibilityAction(View host, int action, Bundle args) {
                //忽略AccessibilityService传过来的点击事件以达到防止模拟点击的目的
                if (action == AccessibilityNodeInfo.ACTION_CLICK
                        || action == AccessibilityNodeInfo.ACTION_LONG_CLICK) {
                    return true;
                }
                return super.performAccessibilityAction(host, action, args);
            }
        });
    }

    protected void checkForceAuth() {
    /*    if (Constants.isForceAuth) {
            String content = getString(R.string.force_auth_content);
            String title = getString(R.string.force_auth_title);
            String msg = getString(R.string.force_auth_msg);

            SpannableString spannable = new SpannableString(content);
            spannable.setSpan(new ForegroundColorSpan(getResources()
                    .getColor(R.color.color_4a)), 0, content.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            spannable.setSpan(new AbsoluteSizeSpan(AppContextUtil.dip2px(18)),
                    content.indexOf(title), content.indexOf(title) + title.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            spannable.setSpan(new AbsoluteSizeSpan(AppContextUtil.dip2px(14)),
                    content.indexOf(msg), content.indexOf(msg) + msg.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

            new CustomAlertDialog.Builder(this)
                    .setCancelable(false)
                    .setTopImage(R.drawable.icon_forceauth_title)
                    .setMessage(spannable)
                    .setPositiveText(R.string.goto_auth)
                    .setPositiveColor(R.color.color_a662ef)
                    .setDialogClickListener(new CustomAlertDialog.DialogInterface() {
                        @Override
                        public void onPositiveClick() {
                            BindPhoneActivity.intentTo(BaseActivity.this);
                        }
                    })
                    .setNegativeText(R.string.logout)
                    .setNegativeColor(R.color.color_56aae7)
                    .setNegativeClickListener(new CustomAlertDialog.DialogNegativeInterface() {
                        @Override
                        public void onNegativeClick() {
                            Constants.isForceAuth = false;
                            LoginManager.clearOnLogout();
                            EventBusManager.getInstance().post(new LogoutEvent());
                            finish();
                            LaunchActivity.intentTo(BaseActivity.this);
                        }
                    })
                    .build().show();
        }*/
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //if (Constants.isForceAuth) return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}

