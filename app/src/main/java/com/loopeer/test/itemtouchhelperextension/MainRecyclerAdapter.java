package com.loopeer.test.itemtouchhelperextension;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Integer> mDatas;
    private Context mContext;

    public MainRecyclerAdapter(Context context) {
        mDatas = new ArrayList<>();
        mContext = context;
    }

    public void setDatas(List<Integer> datas) {
        mDatas.clear();
        mDatas.addAll(datas);
    }

    public void updateData(List<Integer> datas) {
        setDatas(datas);
        notifyDataSetChanged();
    }

    private LayoutInflater getLayoutInflater() {
        return LayoutInflater.from(mContext);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = getLayoutInflater().inflate(R.layout.list_item_main, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((ItemViewHolder) holder).bind(mDatas.get(position));
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView mTextTitle;
        View mViewContent;

        public ItemViewHolder(View itemView) {
            super(itemView);
            mTextTitle = (TextView) itemView.findViewById(R.id.text_list_main_title);
            mViewContent = itemView.findViewById(R.id.view_list_main_content);
        }

        public void bind(Integer integer) {
            mTextTitle.setText("#" + integer);
        }
    }

}
