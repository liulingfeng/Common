package com.llf.common;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.Toast;

import com.llf.basemodel.base.BaseActivity;
import com.llf.basemodel.recycleview.BaseAdapter;
import com.llf.basemodel.recycleview.BaseViewHolder;
import com.llf.basemodel.recycleview.DefaultItemDecoration;
import com.llf.basemodel.recycleview.EndLessOnScrollListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by llf on 2017/3/2.
 */

public class RecycleViewActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener{
    @Bind(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @Bind(R.id.refreshLayout)
    SwipeRefreshLayout mRefreshLayout;

    private List<String> datas = new ArrayList<>();
    private BaseAdapter<String> adapter;
    private ItemTouchHelper mItemTouchHelper;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_recycleview;
    }

    @Override
    protected void initView() {
        datas.add("aa");
        datas.add("ab");
        datas.add("abc");
        datas.add("bf");
        datas.add("bg");
        datas.add("cr");
        datas.add("ds");
        datas.add("dg");
        datas.add("ej");
        datas.add("fy");
        datas.add("fa");
        datas.add("fr");
        mRefreshLayout.setColorSchemeResources(android.R.color.holo_red_light, android.R.color.holo_blue_light, android.R.color.holo_green_light);
        mRefreshLayout.setOnRefreshListener(this);
        mItemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                int fromPosition = viewHolder.getAdapterPosition();
                int toPosition = target.getAdapterPosition();
                datas.add(toPosition, datas.remove(fromPosition));
                adapter.notifyItemMoved(fromPosition, toPosition);
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                datas.remove(position);
                adapter.notifyItemRemoved(position);
                Toast.makeText(RecycleViewActivity.this, "删除" + position, Toast.LENGTH_SHORT).show();
            }
        });
        mItemTouchHelper.attachToRecyclerView(mRecyclerView);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new DefaultItemDecoration(this));
        adapter = new BaseAdapter<String>(RecycleViewActivity.this, R.layout.item_letter, datas) {
            @Override
            public void convert(BaseViewHolder viewHolder, String item) {
                viewHolder.setText(R.id.content, item);
            }
        };
        adapter.addFooterView(R.layout.layout_footer);
        adapter.setOnItemClickLitener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Toast.makeText(RecycleViewActivity.this, position + "被点击了", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemLongClick(int position) {
                Toast.makeText(RecycleViewActivity.this, "长按", Toast.LENGTH_SHORT).show();
            }
        });
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.addOnScrollListener(new EndLessOnScrollListener(manager) {
            @Override
            public void onLoadMore() {
                adapter.setFooterVisible(View.VISIBLE);
                datas.add("加载更多咯");
                adapter.notifyDataSetChanged();
            }
        });
    }


    @Override
    public void onRefresh() {
        mRefreshLayout.setRefreshing(false);
    }
}
