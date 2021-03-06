package com.gank.gankly.ui.discovered.video;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.gank.gankly.R;
import com.gank.gankly.bean.ResultsBean;
import com.gank.gankly.config.MeiziArrayList;
import com.gank.gankly.listener.MeiziOnClick;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 视频
 * Create by LingYan on 2016-04-25
 * Email:137387869@qq.com
 */
public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.GankViewHolder> {
    private List<ResultsBean> mResults;
    private List<ResultsBean> mImagesList;
    private MeiziOnClick mMeiZiOnClick;
    private Context mContext;

    public VideoAdapter(Context context) {
        setHasStableIds(true);
        mResults = new ArrayList<>();
        mContext = context;
    }

    @Override
    public GankViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_video, parent, false);
        return new GankViewHolder(view);
    }

    @Override
    public void onBindViewHolder(GankViewHolder holder, int position) {
        ResultsBean bean = mResults.get(position);
        holder.txtDesc.setText(bean.getDesc());
        holder.txtAuthor.setText(bean.getWho());

        int size = MeiziArrayList.getInstance().getOneItemsList().size();
        if (position > size && size > 0) {
            position = position % size;
        }
        if (position < size && mImagesList != null) {
            Glide.with(mContext)
                    .asBitmap()
                    .load(mImagesList.get(position).getUrl())
                    .apply(new RequestOptions()
                            .fitCenter()
                    )
                    .into(holder.mImageView);
        }
    }

    @Override
    public void onViewRecycled(GankViewHolder holder) {
        super.onViewRecycled(holder);
        Glide.get(mContext).clearMemory();
//        holder.mImageView.setImageBitmap(null);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return mResults.size();
    }

    public void refillItems(List<ResultsBean> getResults) {
        int size = mResults.size();
        mResults.clear();
        notifyItemRangeRemoved(0, size);
        appendItems(getResults);
    }

    public void appendItems(List<ResultsBean> getResults) {
        setImages();

        mResults.addAll(getResults);
        notifyItemRangeInserted(mResults.size(), getResults.size());
    }

    private void setImages() {
        List<ResultsBean> list = MeiziArrayList.getInstance().getOneItemsList();
        mImagesList = new ArrayList<>(list);
        Collections.shuffle(mImagesList);
    }

    public List<ResultsBean> getResults() {
        return mResults;
    }

    public void setOnItemClickListener(MeiziOnClick onItemClickListener) {
        mMeiZiOnClick = onItemClickListener;
    }

    class GankViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.goods_txt_title)
        TextView txtDesc;
        @BindView(R.id.goods_img_bg)
        ImageView mImageView;
        @BindView(R.id.video_txt_author)
        TextView txtAuthor;

        public GankViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onClick(View v) {
            if (mMeiZiOnClick != null) {
                mMeiZiOnClick.onClick(v, getAdapterPosition());
            }
        }
    }
}
