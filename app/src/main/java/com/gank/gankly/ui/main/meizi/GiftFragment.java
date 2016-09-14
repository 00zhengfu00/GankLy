package com.gank.gankly.ui.main.meizi;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gank.gankly.App;
import com.gank.gankly.R;
import com.gank.gankly.RxBus.ChangeThemeEvent.ThemeEvent;
import com.gank.gankly.RxBus.RxBus;
import com.gank.gankly.bean.GiftBean;
import com.gank.gankly.config.ViewsModel;
import com.gank.gankly.listener.ItemClick;
import com.gank.gankly.presenter.GiftPresenter;
import com.gank.gankly.ui.base.BaseSwipeRefreshLayout;
import com.gank.gankly.ui.base.LazyFragment;
import com.gank.gankly.ui.browse.BrowseActivity;
import com.gank.gankly.ui.main.MainActivity;
import com.gank.gankly.utils.StyleUtils;
import com.gank.gankly.view.IGiftView;
import com.gank.gankly.widget.MultipleStatusView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import rx.functions.Action1;

/**
 * 清纯妹子
 * Create by LingYan on 2016-05-17
 * Email:137387869@qq.com
 */
public class GiftFragment extends LazyFragment implements ItemClick, IGiftView {
    private static GiftFragment sGiftFragment;

    @BindView(R.id.meizi_swipe_refresh)
    BaseSwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.loading_view)
    MultipleStatusView mMultipleStatusView;

    private RecyclerView mRecyclerView;
    private GiftAdapter mAdapter;
    private MainActivity mActivity;

    private int mCurPage = 1;
    private List<GiftBean> mImageCountList = new ArrayList<>();
    private ProgressDialog mDialog;
    private GiftPresenter mPresenter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_gift;
    }

    public GiftFragment() {
    }

    public static GiftFragment getInstance() {
        if (sGiftFragment == null) {
            sGiftFragment = new GiftFragment();
        }
        return sGiftFragment;
    }

    @Override
    protected void initPresenter() {
        mPresenter = new GiftPresenter(mActivity, this);
    }

    @Override
    protected void initValues() {
        initRefresh();

        RxBus.getInstance().toSubscription(ThemeEvent.class, new Action1<ThemeEvent>() {
            @Override
            public void call(ThemeEvent event) {
                changeUi();
            }
        });
    }

    @Override
    protected void initViews() {
        initRecycler();
        setSwipeRefreshLayout(mSwipeRefreshLayout);
    }

    @Override
    protected void bindLister() {
        mRecyclerView = mSwipeRefreshLayout.getRecyclerView();
    }

    @Override
    protected void initData() {
        initRefresh();
    }

    private void initRefresh() {
        mMultipleStatusView.showLoading();
        onFetchNew();
    }

    private void changeUi() {
        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = mActivity.getTheme();
        theme.resolveAttribute(R.attr.baseAdapterItemBackground, typedValue, true);
        int background = typedValue.data;
        mSwipeRefreshLayout.getRecyclerView().setBackgroundColor(background);

        TypedValue textValue = new TypedValue();
        theme.resolveAttribute(R.attr.baseAdapterItemTextColor, textValue, true);
        int textColor = textValue.data;

        int childCount = mRecyclerView.getChildCount();
        for (int childIndex = 0; childIndex < childCount; childIndex++) {
            ViewGroup childView = (ViewGroup) mRecyclerView.getChildAt(childIndex);
            TextView title = (TextView) childView.findViewById(R.id.goods_txt_title);
            title.setTextColor(textColor);
            View rlView = childView.findViewById(R.id.goods_rl_title);
            rlView.setBackgroundColor(background);
        }

        StyleUtils.clearRecyclerViewItem(mRecyclerView);
        StyleUtils.changeSwipeRefreshLayout(mSwipeRefreshLayout);
    }

    private void onFetchNew() {
        mCurPage = 1;
        mPresenter.fetchNew(mCurPage);
    }

    private void onFetchNext() {
        showRefresh();
        mPresenter.fetchNext(mCurPage);
    }

    private void initRecycler() {
        mSwipeRefreshLayout.setLayoutManager(new StaggeredGridLayoutManager(2,
                StaggeredGridLayoutManager.VERTICAL));
        mSwipeRefreshLayout.setOnScrollListener(new BaseSwipeRefreshLayout.OnSwipeRefRecyclerViewListener() {
            @Override
            public void onRefresh() {
                onFetchNew();
            }

            @Override
            public void onLoadMore() {
                onFetchNext();
            }
        });

        mSwipeRefreshLayout.setColorSchemeColors(App.getAppColor(R.color.colorPrimary));
        mAdapter = new GiftAdapter(mActivity);
        mAdapter.setOnItemClickListener(this);
        mSwipeRefreshLayout.setAdapter(mAdapter);
    }


    private void showDialog() {
        if (mDialog == null) {
            mDialog = new ProgressDialog(mActivity);
        }

        mDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mDialog.setMessage(App.getAppString(R.string.loading_meizi_images));
        mDialog.setIndeterminate(false);
        mDialog.setCanceledOnTouchOutside(true);
        mDialog.setProgress(0);
        mDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                mPresenter.unSubscribe();
            }
        });

        if (!mDialog.isShowing()) {
            mDialog.show();
        }
    }

    @Override
    public void showContent() {
        mMultipleStatusView.showContent();
    }

    @Override
    public void hideRefresh() {
        super.hideRefresh();
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showRefresh() {
        super.showRefresh();
        mSwipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void clear() {
        mAdapter.clear();
    }

    @Override
    public void refillDate(List<GiftBean> list) {
        mAdapter.updateItems(list);
    }

    @Override
    public void setMax(int max) {
        if (mDialog != null) {
            mDialog.setMax(max);
        }
    }

    @Override
    public void setProgress(int progress) {
        if (mDialog != null) {
            mDialog.setProgress(progress);
        }
    }

    @Override
    public void disDialog() {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }

    @Override
    public void setNextPage(int page) {
        this.mCurPage = page;
    }

    @Override
    public void refillImagesCount(List<GiftBean> giftResult) {
        mImageCountList.addAll(giftResult);
    }

    @Override
    public void gotoBrowseActivity() {
        Bundle bundle = new Bundle();
        Intent intent = new Intent(mActivity, BrowseActivity.class);
        bundle.putString(ViewsModel.Gift, ViewsModel.Gift);
        intent.putExtras(bundle);
        mActivity.startActivity(intent);
    }

    @Override
    public void onClick(int position, Object object) {
        showDialog();
        mImageCountList.clear();
        GiftBean giftBean = (GiftBean) object;
        mPresenter.fetchImagePageList(giftBean.getUrl());
    }

    public List<GiftBean> getList() {
        return mImageCountList;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mActivity = (MainActivity) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);
    }
}
