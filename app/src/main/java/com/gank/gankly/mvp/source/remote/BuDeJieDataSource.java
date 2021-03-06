package com.gank.gankly.mvp.source.remote;

import com.gank.gankly.bean.BuDeJieBean;
import com.gank.gankly.bean.BuDeJieVideo;
import com.gank.gankly.mvp.source.BaseDataSourceModel;
import com.gank.gankly.network.api.ApiManager;
import com.gank.gankly.network.service.BaiSiService;

import io.reactivex.Observable;

/**
 * Create by LingYan on 2016-11-30
 * Email:137387869@qq.com
 */

public class BuDeJieDataSource extends BaseDataSourceModel {
    private static final String BASE_URL = "http://s.budejie.com/";

    private volatile static BuDeJieDataSource INSTANCE = null;
    private BaiSiService mGankService;

    private BuDeJieDataSource() {
        mGankService = ApiManager.init(BASE_URL).createService(BaiSiService.class);
    }

    public static BuDeJieDataSource getInstance() {
        if (INSTANCE == null) {
            synchronized (BuDeJieDataSource.class) {
                if (INSTANCE == null) {
                    INSTANCE = new BuDeJieDataSource();
                }
            }
        }
        return INSTANCE;
    }

    public Observable<BuDeJieBean> fetchImage(int np) {
        return toObservable(mGankService.fetchImage(np, "xiaomi", "6.6.1", "", "6.0.1", "baisibudejie", "android", "866333021430895", "02:00:00:00:00:00", "MI 4LTE", "移动", "1080", "1920", "CN"));
    }

    public Observable<BuDeJieVideo> fetchVideo(int np) {
        return toObservable(mGankService.fetchVideo(np));
    }
}
