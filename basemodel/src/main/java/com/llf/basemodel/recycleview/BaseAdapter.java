package com.llf.basemodel.recycleview;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by llf on 2017/1/3.
 * 基础的adapter
 */

public abstract class BaseAdapter<T> extends RecyclerView.Adapter<BaseViewHolder> {
    private int itemId;
    private Context mContext;
    private List<T> datas;
    private OnItemClickListener mOnItemClickListener;

    private int viewFooter;
    private int viewHead;
    private View footerView;

    private static final int TYPE_NORMAL = 1000;
    private static final int TYPE_HEADER = 1001;
    private static final int TYPE_FOOTER = 1002;

    public BaseAdapter(Context context, int itemId, List<T> datas) {
        this.itemId = itemId;
        this.mContext = context;
        this.datas = datas;
    }

    public void replaceAll(List<T> elements) {
        if (datas.size() > 0) {
            datas.clear();
        }
        datas.addAll(elements);
        notifyDataSetChanged();
    }

    public void addAll(List<T> elements) {
        datas.addAll(elements);
        notifyDataSetChanged();
    }

    public void addHeaderView(int headerView) {
        this.viewHead = headerView;
        notifyItemInserted(0);
    }

    public void addFooterView(int footerView) {
        this.viewFooter = footerView;
        notifyItemInserted(getItemCount() - 1);
    }

    public void setFooterVisible(int visible) {
        footerView.setVisibility(visible);
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_FOOTER) {
            footerView = LayoutInflater.from(mContext).inflate(viewFooter, parent, false);
            return new BaseViewHolder(footerView);
        } else if (viewType == TYPE_HEADER) {
            return new BaseViewHolder(LayoutInflater.from(mContext).inflate(viewHead, parent, false));
        } else {
            return new BaseViewHolder(LayoutInflater.from(mContext).inflate(itemId, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(final BaseViewHolder holder, final int position) {
        if (!isHeaderView(position) && !isFooterView(position)) {
            if (mOnItemClickListener != null) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOnItemClickListener.onItemClick(viewHead == 0 ? position : position - 1, holder);
                    }
                });

                holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        mOnItemClickListener.onItemLongClick(viewHead == 0 ? position : position - 1);
                        return true;
                    }
                });
            }
            convert(holder, datas.get(viewHead == 0 ? position : position - 1));
        }
    }

    @Override
    public int getItemCount() {
        int count = (datas == null ? 0 : datas.size());
        if (viewFooter != 0) {
            count++;
        }

        if (viewHead != 0) {
            count++;
        }
        return count;
    }

    @Override
    public int getItemViewType(int position) {
        if (isHeaderView(position)) {
            return TYPE_HEADER;
        } else if (isFooterView(position)) {
            return TYPE_FOOTER;
        } else {
            return TYPE_NORMAL;
        }
    }

    /**
     * 处理GridLayoutManager
     */
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            final GridLayoutManager gridManager = ((GridLayoutManager) manager);
            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return getItemViewType(position) == TYPE_NORMAL
                            ? 1 : gridManager.getSpanCount();
                }
            });
        }
    }

    /**
     * 处理StaggeredGridLayoutManager
     */
    @Override
    public void onViewAttachedToWindow(BaseViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
        if (lp != null && lp instanceof StaggeredGridLayoutManager.LayoutParams) {
            StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
            p.setFullSpan(holder.getLayoutPosition() == 0);
        }
    }

    private boolean isHeaderView(int position) {
        return viewHead != 0 && position == 0;
    }

    private boolean isFooterView(int position) {
        return viewFooter != 0 && position == getItemCount() - 1;
    }

    //设置点击事件
    public void setOnItemClickLitener(OnItemClickListener mLitener) {
        mOnItemClickListener = mLitener;
    }

    public abstract void convert(BaseViewHolder viewHolder, T item);

    public interface OnItemClickListener {
        void onItemClick(int position, BaseViewHolder holder);
        void onItemLongClick(int position);
    }
}
