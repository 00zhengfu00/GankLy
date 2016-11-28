package com.gank.gankly.ui.main.meizi;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.gank.gankly.App;
import com.gank.gankly.R;
import com.gank.gankly.bean.ResultsBean;
import com.gank.gankly.listener.MeiziOnClick;
import com.gank.gankly.presenter.IBaseRefreshPresenter;
import com.gank.gankly.presenter.impl.MeiziPresenterImpl;
import com.gank.gankly.ui.base.LazyFragment;
import com.gank.gankly.ui.gallery.GalleryActivity;
import com.gank.gankly.ui.main.HomeActivity;
import com.gank.gankly.utils.CircularAnimUtils;
import com.gank.gankly.utils.StyleUtils;
import com.gank.gankly.utils.theme.ThemeUtils;
import com.gank.gankly.view.IMeiziView;
import com.gank.gankly.widget.LySwipeRefreshLayout;
import com.gank.gankly.widget.MultipleStatusView;

import java.util.List;

import butterknife.BindView;

/**
 * 福利妹子
 * Create by LingYan on 2016-5-12
 * Email:137387869@qq.com
 */
public class MeiZiFragment extends LazyFragment implements MeiziOnClick, SwipeRefreshLayout.OnRefreshListener,
        IMeiziView<List<ResultsBean>> {
    @BindView(R.id.multiple_status_view)
    MultipleStatusView mMultipleStatusView;
    @BindView(R.id.swipe_refresh)
    LySwipeRefreshLayout mSwipeRefreshLayout;

    private MeiZiRecyclerAdapter mMeiZiRecyclerAdapter;
    private HomeActivity mActivity;
    private RecyclerView mRecyclerView;

    private IBaseRefreshPresenter mPresenter;

    @Override
    protected int getLayoutId() {
        return R.layout.layout_swipe_normal;
    }

    public MeiZiFragment() {
    }

    public static MeiZiFragment newInstance() {
        MeiZiFragment fragment = new MeiZiFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initValues() {
    }

    @Override
    protected void initViews() {
        setSwipeRefreshLayout(mSwipeRefreshLayout);

        mMeiZiRecyclerAdapter = new MeiZiRecyclerAdapter(mActivity);
        mMeiZiRecyclerAdapter.setMeiZiOnClick(this);
        mRecyclerView = mSwipeRefreshLayout.getRecyclerView();
        mSwipeRefreshLayout.setLayoutManager(new StaggeredGridLayoutManager(2,
                StaggeredGridLayoutManager.VERTICAL));
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setAdapter(mMeiZiRecyclerAdapter);
    }

    @Override
    protected void bindListener() {
        mMultipleStatusView.setListener(new MultipleStatusView.OnMultipleClick() {
            @Override
            public void retry(View v) {
                mMultipleStatusView.showLoading();
                fetchNew();
            }
        });

        mSwipeRefreshLayout.setOnScrollListener(new LySwipeRefreshLayout.OnSwipeRefRecyclerViewListener() {
            @Override
            public void onRefresh() {
                fetchNew();
            }

            @Override
            public void onLoadMore() {
                onNextRefresh();
            }
        });
    }

    @Override
    protected void initData() {
        mMultipleStatusView.showLoading();
        fetchNew();
    }

    @Override
    protected void initPresenter() {
        mPresenter = new MeiziPresenterImpl(mActivity, this);
    }

    private void onNextRefresh() {
        fetchMore();
    }

    @Override
    public void onRefresh() {
        mSwipeRefreshLayout.setRefreshing(true);
        fetchNew();
    }

    @Override
    public void refillDate(List<ResultsBean> list) {
        mMeiZiRecyclerAdapter.updateItems(list);
    }

    @Override
    public void appendMoreDate(List<ResultsBean> list) {
        mMeiZiRecyclerAdapter.addItems(list);
    }

    @Override
    public void hasNoMoreDate() {
        Snackbar.make(mSwipeRefreshLayout, R.string.tip_no_more_load, Snackbar.LENGTH_LONG)
                .setActionTextColor(App.getAppColor(R.color.Blue))
                .show();
    }

    @Override
    public void showRefreshError(String errorStr) {
        Snackbar.make(mSwipeRefreshLayout, errorStr, Snackbar.LENGTH_LONG)
                .setActionTextColor(App.getAppColor(R.color.Blue))
                .setAction(R.string.retry, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        fetchMore();
                    }
                }).show();
    }

    private void fetchNew() {
        mPresenter.fetchNew();
    }

    private void fetchMore() {
        mPresenter.fetchMore();
    }

    @Override
    public void hideRefresh() {
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showRefresh() {
        mSwipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void showContent() {
        super.showContent();
        mMultipleStatusView.showContent();
    }

    @Override
    public void showDisNetWork() {
        super.showDisNetWork();
        mMultipleStatusView.showNoNetwork();
    }

    @Override
    public void showEmpty() {
        super.showEmpty();
        mMultipleStatusView.showEmpty();
    }

    @Override
    public void showError() {
        super.showError();
        mMultipleStatusView.showError();
    }

    @Override
    public void clear() {
        mMeiZiRecyclerAdapter.clear();
    }

    @Override
    public void onClick(View view, int position) {
        Bundle bundle = new Bundle();
        bundle.putInt(GalleryActivity.EXTRA_POSITION, position);
        Intent intent = new Intent(mActivity, GalleryActivity.class);
        intent.putExtra(GalleryActivity.TAG, bundle);
        CircularAnimUtils.startActivity(mActivity, intent, view, R.color.color_2f);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mActivity = (HomeActivity) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    protected void callBackRefreshUi() {
        ThemeUtils themeUtils = new ThemeUtils(this);
        int resource = themeUtils.getResourceId(R.attr.themeBackground);
        mRecyclerView.setBackgroundResource(resource);

        StyleUtils.clearRecyclerViewItem(mRecyclerView);
        StyleUtils.changeSwipeRefreshLayout(mSwipeRefreshLayout);
    }
}
