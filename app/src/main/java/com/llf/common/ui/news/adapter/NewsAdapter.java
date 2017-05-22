package com.llf.common.ui.news.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.llf.basemodel.recycleview.BaseViewHolder;
import com.llf.basemodel.utils.ImageLoaderUtils;
import com.llf.basemodel.utils.LogUtil;
import com.llf.basemodel.utils.SettingUtil;
import com.llf.common.R;
import com.llf.common.entity.NewsEntity;

import java.util.List;

/**
 * Created by llf on 2017/4/20.
 * 新闻item分一张图和多张图的形式
 */

public class NewsAdapter extends RecyclerView.Adapter<BaseViewHolder> {
    private static final int TYPE_FOOTER = 0;
    private static final int ITEM_IMAGE = 1;
    private static final int ITEM_IMAGES = 2;

    private List<NewsEntity> datas;
    private Context mContext;
    private int viewFooter;
    private View footerView;
    private OnItemClickListener mOnItemClickListener;
    private int itemWidth;

    public NewsAdapter(List<NewsEntity> datas, Context context) {
        this.datas = datas;
        this.mContext = context;
        itemWidth = (SettingUtil.getScreenWidth(context) - SettingUtil.dip2px(context, 32)) / 3;
    }

    public void replaceAll(List<NewsEntity> elements) {
        if (datas.size() > 0) {
            datas.clear();
        }
        datas.addAll(elements);
        notifyDataSetChanged();
    }

    public void addAll(List<NewsEntity> elements) {
        datas.addAll(elements);
        notifyDataSetChanged();
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_IMAGE) {
            return new BaseViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_news, parent, false));
        } else if (viewType == ITEM_IMAGES) {
            return new BaseViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_news_images, parent, false));
        } else {
            footerView = LayoutInflater.from(mContext).inflate(viewFooter, parent, false);
            return new BaseViewHolder(footerView);
        }
    }

    @Override
    public void onBindViewHolder(final BaseViewHolder holder, final int position) {
        if (!(viewFooter != 0 && position == getItemCount() - 1)) {
            int type = getItemViewType(position);
            if (mOnItemClickListener != null) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOnItemClickListener.onItemClick(position, holder);
                    }
                });
            }

            NewsEntity item = datas.get(position);
            try {
                if (type == ITEM_IMAGE) {
                    ImageView imageView = holder.getView(R.id.ivNews);
                    holder.setText(R.id.tvTitle, item.getTitle());
                    holder.setText(R.id.tvDesc, item.getDigest());
                    ImageLoaderUtils.loadingImg(mContext, imageView, item.getImgsrc());
                } else {
                    holder.setText(R.id.tvTitle, item.getTitle());
                    LinearLayout images = holder.getView(R.id.images);
                    images.removeAllViews();
                    for (int i = 0; i < item.getImgextra().size(); i++) {
                        ImageView icon = new ImageView(mContext);
                        LinearLayout.LayoutParams paras = new LinearLayout.LayoutParams(itemWidth, itemWidth);
                        if (i == 1) {
                            paras.setMargins(SettingUtil.dip2px(mContext, 4), 0, SettingUtil.dip2px(mContext, 4), 0);
                        }
                        icon.setLayoutParams(paras);
                        ImageLoaderUtils.loadingImg(mContext, icon, item.getImgextra().get(i).getImgsrc());
                        images.addView(icon);
                    }
                }
            } catch (Exception e) {
                LogUtil.d("文字内容为空");
            }
        }
    }

    @Override
    public int getItemCount() {
        int count = (datas == null ? 0 : datas.size());
        if (viewFooter != 0) {
            count++;
        }
        return count;
    }

    @Override
    public int getItemViewType(int position) {
        int type = ITEM_IMAGE;
        if (viewFooter != 0 && position == getItemCount() - 1) {
            type = TYPE_FOOTER;
            return type;
        }
        if (datas.get(position).getImgextra() == null) {
            type = ITEM_IMAGE;
        } else {
            type = ITEM_IMAGES;
        }
        return type;
    }

    public void addFooterView(int footerView) {
        this.viewFooter = footerView;
        notifyItemInserted(getItemCount() - 1);
    }

    public void setFooterVisible(int visible) {
        footerView.setVisibility(visible);
    }

    //设置点击事件
    public void setOnItemClickLitener(OnItemClickListener mLitener) {
        mOnItemClickListener = mLitener;
    }

    public interface OnItemClickListener {
        void onItemClick(int position, BaseViewHolder viewHolder);
    }
}
