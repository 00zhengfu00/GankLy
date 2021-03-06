package com.gank.gankly.ui.girls.dailymeizi;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gank.gankly.App;
import com.gank.gankly.R;
import com.gank.gankly.rxjava.RxBus_;
import com.gank.gankly.rxjava.theme.ThemeEvent;
import com.gank.gankly.bean.DailyMeiziBean;
import com.gank.gankly.bean.GiftBean;
import com.gank.gankly.listener.ItemClick;
import com.gank.gankly.mvp.source.remote.MeiziDataSource;
import com.gank.gankly.ui.base.LazyFragment;
import com.gank.gankly.ui.gallery.GalleryActivity;
import com.gank.gankly.ui.main.MainActivity;
import com.gank.gankly.utils.StyleUtils;
import com.gank.gankly.widget.LySwipeRefreshLayout;
import com.gank.gankly.widget.MultipleStatusView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.disposables.Disposable;

/**
 * 妹子每日更新
 * Create by LingYan on 2016-07-01
 * Email:137387869@qq.com
 */
public class DailyMeiziFragment extends LazyFragment implements DailyMeiziContract.View, ItemClick {
    @BindView(R.id.multiple_status_view)
    MultipleStatusView mMultipleStatusView;
    @BindView(R.id.meizi_swipe_refresh)
    LySwipeRefreshLayout mSwipeRefreshLayout;

    private DailyMeiziAdapter mDailyMeiziAdapter;
    private DailyMeiziPresenter mPresenter;
    private MainActivity mActivity;
    private Disposable mDisposable;

    private ProgressDialog mDialog;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_gift;
    }

    @Override
    protected void initPresenter() {
        mPresenter = new DailyMeiziPresenter(MeiziDataSource.getInstance(), this);
    }

    @Override
    protected void initValues() {
        mDisposable = RxBus_.getInstance().toObservable(ThemeEvent.class)
                .subscribe(themeEvent -> changeUi());
    }

    @Override
    protected void initViews() {
        mDailyMeiziAdapter = new DailyMeiziAdapter();
        mSwipeRefreshLayout.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));
        mSwipeRefreshLayout.setAdapter(mDailyMeiziAdapter);
        setSwipeRefreshLayout(mSwipeRefreshLayout);
    }

    @Override
    protected void bindListener() {
        mSwipeRefreshLayout.setOnScrollListener(new LySwipeRefreshLayout.OnSwipeRefRecyclerViewListener() {
            @Override
            public void onRefresh() {
                mPresenter.fetchNew();
            }

            @Override
            public void onLoadMore() {
//                mPresenter.fetchMore();
            }
        });

        mDailyMeiziAdapter.setOnItemClickListener(this);
    }

    @Override
    protected void initData() {
        mMultipleStatusView.showLoading();
        mPresenter.fetchNew();
    }

    private void changeUi() {
        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = mActivity.getTheme();
        theme.resolveAttribute(R.attr.meiziDailyItemBackground, typedValue, true);
        int background = typedValue.data;
        TypedValue textValue = new TypedValue();
        theme.resolveAttribute(R.attr.baseAdapterItemTextColor, textValue, true);
        int textColor = textValue.data;
        theme.resolveAttribute(R.attr.themeBackground, textValue, true);
        int recyclerColor = textValue.data;
        mSwipeRefreshLayout.getRecyclerView().setBackgroundColor(recyclerColor);

        int childCount = mSwipeRefreshLayout.getRecyclerView().getChildCount();
        for (int childIndex = 0; childIndex < childCount; childIndex++) {
            ViewGroup childView = (ViewGroup) mSwipeRefreshLayout.getRecyclerView().getChildAt(childIndex);
            childView.setBackgroundColor(background);
            TextView title = (TextView) childView.findViewById(R.id.daily_meizi_title);
            title.setTextColor(textColor);
        }

        StyleUtils.clearRecyclerViewItem(mSwipeRefreshLayout.getRecyclerView());
        StyleUtils.changeSwipeRefreshLayout(mSwipeRefreshLayout);
    }

    private void showLoadingDialog() {
        if (mDialog == null) {
            mDialog = new ProgressDialog(mActivity);
        }
        mDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mDialog.setMessage(App.getAppString(R.string.loading_meizi_images));
        mDialog.setIndeterminate(true);
        mDialog.setCanceledOnTouchOutside(true);
        mDialog.setOnCancelListener(dialog -> mPresenter.unSubscribe());
        if (!mDialog.isShowing()) {
            mDialog.show();
        }
    }

    @Override
    public void openBrowseActivity(@NonNull ArrayList<GiftBean> list) {
        Bundle bundle = new Bundle();
        Intent intent = new Intent(mActivity, GalleryActivity.class);
        bundle.putString(GalleryActivity.EXTRA_MODEL, GalleryActivity.EXTRA_DAILY);
        intent.putExtra(GalleryActivity.EXTRA_LIST, list);
        intent.putExtras(bundle);
        mActivity.startActivity(intent);
    }

    @Override
    public void hideRefresh() {
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void hasNoMoreDate() {

    }

    @Override
    public void showRefresh() {

    }

    @Override
    public void showRefreshError(String errorStr) {

    }

    @Override
    public void showContent() {
        mMultipleStatusView.showContent();
    }

    @Override
    public void showEmpty() {

    }

    @Override
    public void showDisNetWork() {

    }

    @Override
    public void showError() {

    }

    @Override
    public void showLoading() {
        mSwipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void onClick(int position, Object object) {
        DailyMeiziBean dailyMeiziBean = (DailyMeiziBean) object;
        String url = dailyMeiziBean.getUrl();
        if (!TextUtils.isEmpty(url)) {
            showLoadingDialog();
            mPresenter.girlsImages(url);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (MainActivity) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void refillData(List<DailyMeiziBean> list) {
        mDailyMeiziAdapter.refillItem(list);
    }

    @Override
    public void appendItem(List<DailyMeiziBean> list) {
        mDailyMeiziAdapter.appendItem(list);
    }

    @Override
    public void setMaxProgress(int value) {
        if (mDialog != null) {
            mDialog.setMax(value);
        }
    }

    @Override
    public void disProgressDialog() {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }

    @Override
    protected void callBackRefreshUi() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.dispose();
        }
    }
}
