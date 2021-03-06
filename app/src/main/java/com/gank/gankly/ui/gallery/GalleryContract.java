package com.gank.gankly.ui.gallery;

import com.gank.gankly.bean.ResultsBean;
import com.gank.gankly.mvp.IFetchPresenter;
import com.gank.gankly.mvp.IFetchView;

import java.util.List;

/**
 * Create by LingYan on 2017-01-16
 * Email:137387869@qq.com
 */

public interface GalleryContract {

    interface View extends IFetchView {
        void appendData(List<ResultsBean> list);

        void sysNumText();
    }

    interface Presenter extends IFetchPresenter {

    }
}
