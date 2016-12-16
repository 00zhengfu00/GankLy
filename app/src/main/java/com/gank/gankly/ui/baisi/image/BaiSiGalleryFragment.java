package com.gank.gankly.ui.baisi.image;

import android.graphics.PointF;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.ImageViewState;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.gank.gankly.R;
import com.gank.gankly.mvp.base.BaseFragment;
import com.socks.library.KLog;

import java.io.File;

import butterknife.BindView;

/**
 * Create by LingYan on 2016-12-05
 * Email:137387869@qq.com
 */

public class BaiSiGalleryFragment extends BaseFragment {
    public static final String URL = "BaiSi_Url";
    public static final String SIZE_HEIGHT = "Height";
    public static final String SIZE_WIDTH = "Width";


    @BindView(R.id.largetImageview)
    SubsamplingScaleImageView sliderIv;
    @BindView(R.id.baisi_gallery_img)
    ImageView mImageView;

    private String mUrl;
    private int mHeight;
    private int mWidth;

    public static BaiSiGalleryFragment newInstance(GallerySize gallerySize) {

        Bundle args = new Bundle();
        args.putString(URL, gallerySize.getUrl());
        args.putInt(SIZE_HEIGHT, gallerySize.getHeight());
        args.putInt(SIZE_WIDTH, gallerySize.getWidth());
        BaiSiGalleryFragment fragment = new BaiSiGalleryFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_baisi_gallery;
    }

    @Override
    protected void initValues() {
        sliderIv.setMinimumScaleType(SubsamplingScaleImageView.SCALE_TYPE_CENTER_CROP);
        sliderIv.setDoubleTapZoomStyle(SubsamplingScaleImageView.ZOOM_FOCUS_CENTER);
//        sliderIv.setMinimumScaleType(SubsamplingScaleImageView.SCALE_TYPE_CUSTOM);
//        sliderIv.setMinScale(0.0F);
//        sliderIv.setMaxScale(3.0F);//必须设置
        Bundle bundle = getArguments();
        if (bundle != null) {
            mUrl = bundle.getString(URL);
            mWidth = bundle.getInt(SIZE_WIDTH);
            mHeight = bundle.getInt(SIZE_HEIGHT);
        }
        KLog.d("mWidth:" + mWidth + ",mHeight:" + mHeight);
        loadImageBitmap(mUrl);
    }

    public void loadImageBitmap(final String url) {
        if (url.endsWith(".gif")) {
            sliderIv.setVisibility(View.GONE);
            mImageView.setVisibility(View.VISIBLE);
            Glide.with(this)
                    .load(mUrl)
                    .asGif()
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE) // gif must be add this
                    .into(mImageView);
        } else if (url.endsWith(".png") || url.endsWith(".jpg")) {
            ViewGroup.LayoutParams layoutParams = sliderIv.getLayoutParams();
            layoutParams.width = 1080;
            sliderIv.setLayoutParams(layoutParams);

            Glide.with(this).load(mUrl).downloadOnly(new SimpleTarget<File>() {
                @Override
                public void onResourceReady(File resource, GlideAnimation<? super File> glideAnimation) {
                    //设置图片初始化状态 = 从顶部开始加载
                    ImageViewState state = new ImageViewState(0, new PointF(0, 0), 0);
                    if (mHeight < 1920 || mWidth > 1080) {
                        mImageView.setImageURI(Uri.fromFile(resource));
                    } else {
                        mImageView.setVisibility(View.GONE);
                        sliderIv.setVisibility(View.VISIBLE);
                        sliderIv.setImage(ImageSource.uri(Uri.fromFile(resource).getPath()), state);
                    }
                }
            });
        }
    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void bindLister() {

    }
}