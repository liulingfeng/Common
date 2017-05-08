package com.llf.photopicker.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.llf.basemodel.utils.ImageLoaderUtils;
import com.llf.photopicker.R;
import com.llf.photopicker.bean.Folder;
import java.util.List;

/**
 * Created by llf on 2016/10/18.
 */

public class FolderListAdapter extends BaseAdapter{
    private Context context;
    private List<Folder> folderList;
    private int selected = 0;

    public FolderListAdapter(Context context,List<Folder> folderList){
        this.context = context;
        this.folderList = folderList;
    }
    @Override
    public int getCount() {
        return folderList.size();
    }

    @Override
    public Object getItem(int i) {
        return folderList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null){
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_img_sel_foder,parent,false);
            holder.ivFolder = (ImageView)convertView.findViewById(R.id.ivFolder);
            holder.tvFolderName = (TextView)convertView.findViewById(R.id.tvFolderName);
            holder.indicator = (ImageView)convertView.findViewById(R.id.indicator);
            holder.tvImageNum = (TextView)convertView.findViewById(R.id.tvImageNum);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder)convertView.getTag();
        }

        if (position == 0) {
            holder.tvFolderName.setText("所有图片");
            holder.tvImageNum.setText("共" + getTotalImageSize() + "张");
            ImageView ivFolder = holder.ivFolder;
            if (folderList.size() > 1) {
                ImageLoaderUtils.loadingImg(context,ivFolder,folderList.get(position+1).cover.path);
            }
        } else {
            holder.tvFolderName.setText(folderList.get(position).name);
            holder.tvImageNum.setText("共" + folderList.get(position).images.size() + "张");
            ImageView ivFolder = holder.ivFolder;
            if (folderList.size() > 0) {
                ImageLoaderUtils.loadingImg(context,ivFolder,folderList.get(position).cover.path);
            }
        }

        if (selected == position) {
            holder.indicator.setVisibility(View.VISIBLE);
        } else {
            holder.indicator.setVisibility(View.GONE);
        }
        return convertView;
    }

    public class ViewHolder {
        ImageView ivFolder;
        TextView tvFolderName;
        TextView tvImageNum;
        ImageView indicator;
    }

    private int getTotalImageSize() {
        int result = 0;
        if (folderList != null && folderList.size() > 0) {
            for (Folder folder : folderList) {
                if(folder.images!=null)
                    result += folder.images.size();
            }
        }
        return result;
    }
    public void setSelectIndex(int position) {
        if (selected == position)
            return;
        selected = position;
        notifyDataSetChanged();
    }

    public int getSelectIndex() {
        return selected;
    }
}
