package com.llf.photopicker.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import com.llf.basemodel.recycleview.BaseViewHolder;
import com.llf.basemodel.utils.ImageLoaderUtils;
import com.llf.photopicker.ImgSelConfig;
import com.llf.photopicker.R;
import com.llf.photopicker.bean.Image;
import java.util.ArrayList;
import java.util.List;

public class ImageListAdapter extends RecyclerView.Adapter<BaseViewHolder>{
    private List<Image> datas;
    private Context context;
    private boolean showCamera;
    private boolean mutiSelect;
    private List<Image> selectedImageList = new ArrayList<>();
    private OnItemClickListener mOnItemClickListener;

    public ImageListAdapter(Context context,List<Image> datas, ImgSelConfig config,OnItemClickListener mOnItemClickListener) {
        this.context = context;
        this.datas = datas;
        this.showCamera = config.needCamera;
        this.mutiSelect = config.multiSelect;
        this.mOnItemClickListener = mOnItemClickListener;
    }
    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BaseViewHolder(LayoutInflater.from(context).inflate(R.layout.item_img_sel, parent, false));
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, final int position) {
        ImageView ivImage = holder.getView(R.id.ivImage);
        ImageView ivPhotoCheaked = holder.getView(R.id.ivPhotoCheaked);
        FrameLayout fl_shade = holder.getView(R.id.pi_picture_choose_item_select);
        if (position == 0&& showCamera) {
            ivImage.setImageResource(R.drawable.ic_take_photo);
            ivPhotoCheaked.setVisibility(View.GONE);
        }else{
            ivPhotoCheaked.setVisibility(View.VISIBLE);
            ImageLoaderUtils.loadingImg(context,ivImage,datas.get(position).path);
        }

        if (mutiSelect) {
            if (selectedImageList.contains(datas.get(position))) {
                ivPhotoCheaked.setImageResource(R.drawable.ic_checked);
                fl_shade.setVisibility(View.VISIBLE);
            } else {
                ivPhotoCheaked.setImageResource(R.drawable.ic_uncheck);
                fl_shade.setVisibility(View.GONE);
            }
        } else {
            fl_shade.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickListener.onItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    public void select(Image image) {
        if (selectedImageList.contains(image)) {
            selectedImageList.remove(image);
        } else {
            selectedImageList.add(image);
        }
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
        void onItemLongClick(int position);
    }
}
