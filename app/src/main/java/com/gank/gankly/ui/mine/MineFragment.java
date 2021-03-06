package com.gank.gankly.ui.mine;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.widget.TextView;

import com.gank.gankly.App;
import com.gank.gankly.R;
import com.gank.gankly.rxjava.RxBus_;
import com.gank.gankly.rxjava.theme.ThemeEvent;
import com.gank.gankly.ui.base.BaseSwipeRefreshFragment;
import com.gank.gankly.ui.main.MainActivity;
import com.gank.gankly.ui.more.MoreActivity;
import com.gank.gankly.utils.GanklyPreferences;
import com.gank.gankly.widget.LSwitch;
import com.gank.gankly.widget.LYRelativeLayoutRipple;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.gank.gankly.ui.more.SettingFragment.IS_NIGHT;


/**
 * 我的
 * Create by LingYan on 2016-09-21
 * Email:137387869@qq.com
 */

public class MineFragment extends BaseSwipeRefreshFragment {
    public static final String TAG = "MineFragment";

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.mine_ls_theme)
    LSwitch themeSwitch;
    @BindView(R.id.mine_nested_scroll)
    NestedScrollView mNestedScrollView;
    @BindViews({R.id.mine_txt_browse, R.id.mine_txt_setting, R.id.mine_txt_collect, R.id.mine_txt_night})
    List<TextView> mTextViewList;
    @BindViews({R.id.mine_ls_theme})
    List<LSwitch> mSwitchList;
    @BindViews({R.id.mine_ll_notes, R.id.mine_ll_setting})
    List<LinearLayoutCompat> mLinearLayoutCompatList;
    @BindViews({R.id.mine_rl_collect, R.id.mine_rl_night, R.id.mine_rl_setting, R.id.mine_rl_browse})
    List<LYRelativeLayoutRipple> mViewList;

    private MainActivity mActivity;
    private boolean isNight;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_mine;
    }

    @Override
    protected void initPresenter() {

    }

    @Override
    protected void initValues() {
        selectItemSwitch();

        mNestedScrollView.setNestedScrollingEnabled(false);
    }

    @Override
    protected void initViews() {
        mToolbar.setTitle(R.string.navigation_mine);
    }

    @Override
    protected void bindListener() {
        themeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> selectTheme(isChecked));
    }

    private void selectTheme(boolean isChecked) {
        App.setIsNight(isChecked);
        GanklyPreferences.putBoolean(IS_NIGHT, isChecked);

        if (isChecked) {
            mActivity.setTheme(R.style.AppTheme_Night);
        } else {
            mActivity.setTheme(R.style.AppTheme_light);
        }
        themeSwitch.setChecked(isChecked);
        RxBus_.getInstance().post(new ThemeEvent(isChecked));
        refreshStatusBar();
        callBackRefreshUi();
    }

    private void selectItemSwitch() {
        boolean isNight = App.isNight();
        themeSwitch.setChecked(isNight);
    }


    /**
     * 刷新 StatusBar
     */
    private void refreshStatusBar() {
        if (Build.VERSION.SDK_INT >= 21) {
            TypedValue typedValue = new TypedValue();
            mActivity.getTheme().resolveAttribute(R.attr.colorPrimaryDark, typedValue, true);
            mActivity.getWindow().setStatusBarColor(getResources().getColor(typedValue.resourceId));
        }
    }

    @OnClick(R.id.mine_rl_setting)
    void onSetting() {
        openActivity(MoreActivity.TYPE_SETTING);
    }

//    public void restart() {
//        Intent intent = new Intent(mActivity, SplashActivity.class);
//        intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
//        this.startActivity(intent);
//        mActivity.finish();
//    }

    @OnClick(R.id.mine_rl_night)
    void onNight() {
        isNight = !App.isNight();
        selectTheme(isNight);
    }

    @OnClick(R.id.mine_rl_collect)
    void onCollect() {
        openActivity(MoreActivity.TYPE_COLLECT);
    }

    @OnClick(R.id.mine_rl_browse)
    void onBrowse() {
        openActivity(MoreActivity.TYPE_BROWSE);
    }

    private void openActivity(int type) {
        Intent intent = new Intent();
        intent.setClass(mActivity, MoreActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(MoreActivity.TYPE, type);
        intent.putExtras(bundle);
        mActivity.startActivity(intent);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (MainActivity) context;
    }

    @Override
    protected void callBackRefreshUi() {

        TypedValue typeValue = new TypedValue();
        Resources.Theme theme = mActivity.getTheme();
        theme.resolveAttribute(R.attr.themeBackground, typeValue, true);
        mNestedScrollView.setBackgroundResource(typeValue.resourceId);
        theme.resolveAttribute(R.attr.colorPrimary, typeValue, true);
        mToolbar.setBackgroundResource(typeValue.resourceId);
        theme.resolveAttribute(R.attr.textPrimaryColor, typeValue, true);
        final int itemTextColor = typeValue.resourceId;
        theme.resolveAttribute(R.attr.baseAdapterItemBackground, typeValue, true);
        final int itemBackground = typeValue.resourceId;

        setItemBackground(mViewList);

        ButterKnife.apply(mSwitchList, new ButterKnife.Action<LSwitch>() {
            @Override
            public void apply(@NonNull LSwitch view, int index) {
                view.changeTheme();
            }
        });

        ButterKnife.apply(mTextViewList, new ButterKnife.Action<TextView>() {
            @Override
            public void apply(@NonNull TextView view, int index) {
                view.setTextColor(App.getAppColor(itemTextColor));
            }
        });

        ButterKnife.apply(mLinearLayoutCompatList, new ButterKnife.Action<LinearLayoutCompat>() {
            @Override
            public void apply(@NonNull LinearLayoutCompat view, int index) {
                view.setBackgroundResource(itemBackground);
            }
        });
    }
}
