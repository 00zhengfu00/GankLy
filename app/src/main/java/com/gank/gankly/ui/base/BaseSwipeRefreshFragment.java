package com.gank.gankly.ui.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.gank.gankly.ui.presenter.BasePresenter;
import com.gank.gankly.ui.view.ISwipeRefreshView;


/**
 * Create by LingYan on 2016-04-05
 */
public abstract class BaseSwipeRefreshFragment<P extends BasePresenter> extends BaseFragment<P>
        implements ISwipeRefreshView {

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        initPresenter();
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    protected abstract void initPresenter();

    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void onSuccess() {

    }

    @Override
    public void hideRefresh() {

    }

    @Override
    public void showRefresh() {

    }

    @Override
    public void showEmpty() {

    }

    @Override
    public void showDisNetWork() {

    }

    @Override
    public boolean isDisNetWorkView() {
        return false;
    }

    @Override
    public boolean isEmptyView() {
        return false;
    }

    @Override
    public boolean isLoading() {
        return false;
    }

    @Override
    public boolean isShowView() {
        return false;
    }
}