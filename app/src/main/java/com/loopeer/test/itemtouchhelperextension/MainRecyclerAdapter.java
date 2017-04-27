package com.loopeer.test.itemtouchhelperextension;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.loopeer.itemtouchhelperextension.Extension;
import com.loopeer.itemtouchhelperextension.ItemTouchHelperExtension;

import java.util.ArrayList;
import java.util.List;

public class MainRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int ITEM_TYPE_RECYCLER_WIDTH = 1000;
    public static final int ITEM_TYPE_ACTION_WIDTH = 1001;
    public static final int ITEM_TYPE_ACTION_WIDTH_NO_SPRING = 1002;
    public static final int ITEM_TYPE_NO_SWIPE = 1003;
    private List<TestModel> mDatas;
    private Context mContext;
    private ItemTouchHelperExtension mItemTouchHelperExtension;

    public MainRecyclerAdapter(Context context) {
        mDatas = new ArrayList<>();
        mContext = context;
    }

    public void setDatas(List<TestModel> datas) {
        mDatas.clear();
        mDatas.addAll(datas);
    }

    public void updateData(List<TestModel> datas) {
        setDatas(datas);
        notifyDataSetChanged();
    }

    public void setItemTouchHelperExtension(ItemTouchHelperExtension itemTouchHelperExtension) {
        mItemTouchHelperExtension = itemTouchHelperExtension;
    }

    private LayoutInflater getLayoutInflater() {
        return LayoutInflater.from(mContext);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = getLayoutInflater().inflate(R.layout.list_item_main, parent, false);
        if (viewType == ITEM_TYPE_ACTION_WIDTH) return new ItemSwipeWithActionWidthViewHolder(view);
        if (viewType == ITEM_TYPE_NO_SWIPE) return new ItemNoSwipeViewHolder(view);
        if (viewType == ITEM_TYPE_RECYCLER_WIDTH) {
            view = getLayoutInflater().inflate(R.layout.list_item_with_single_delete, parent, false);
            return new ItemViewHolderWithRecyclerWidth(view);
        }
        return new ItemSwipeWithActionWidthNoSpringViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        ItemBaseViewHolder baseViewHolder = (ItemBaseViewHolder) holder;
        baseViewHolder.bind(mDatas.get(position));
        baseViewHolder.mViewContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "Item Content click: #" + holder.getAdapterPosition(), Toast.LENGTH_SHORT).show();
            }
        });
        if (holder instanceof ItemViewHolderWithRecyclerWidth) {
            ItemViewHolderWithRecyclerWidth viewHolder = (ItemViewHolderWithRecyclerWidth) holder;
            viewHolder.mActionViewDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    doDelete(holder.getAdapterPosition());
                }
            });
        } else if (holder instanceof ItemSwipeWithActionWidthViewHolder) {
            ItemSwipeWithActionWidthViewHolder viewHolder = (ItemSwipeWithActionWidthViewHolder) holder;
            viewHolder.mActionViewRefresh.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Toast.makeText(mContext, "Refresh Click" + holder.getAdapterPosition()
                                    , Toast.LENGTH_SHORT).show();
                            mItemTouchHelperExtension.closeOpened();
                        }
                    }

            );
            viewHolder.mActionViewDelete.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            doDelete(holder.getAdapterPosition());
                        }
                    }

            );
        }
    }

    private void doDelete(int adapterPosition) {
        mDatas.remove(adapterPosition);
        notifyItemRemoved(adapterPosition);
    }

    public void move(int from, int to) {
        TestModel prev = mDatas.remove(from);
        mDatas.add(to > from ? to - 1 : to, prev);
        notifyItemMoved(from, to);
    }

    @Override
    public int getItemViewType(int position) {
        if (mDatas.get(position).position == 1) {
            return ITEM_TYPE_ACTION_WIDTH_NO_SPRING;
        }
        if (mDatas.get(position).position == 2) {
            return ITEM_TYPE_RECYCLER_WIDTH;
        }
        if (mDatas.get(position).position == 3) {
            return ITEM_TYPE_NO_SWIPE;
        }
        return ITEM_TYPE_ACTION_WIDTH;
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    class ItemBaseViewHolder extends RecyclerView.ViewHolder {
        TextView mTextTitle;
        TextView mTextIndex;
        View mViewContent;
        View mActionContainer;

        public ItemBaseViewHolder(View itemView) {
            super(itemView);
            mTextTitle = (TextView) itemView.findViewById(R.id.text_list_main_title);
            mTextIndex = (TextView) itemView.findViewById(R.id.text_list_main_index);
            mViewContent = itemView.findViewById(R.id.view_list_main_content);
            mActionContainer = itemView.findViewById(R.id.view_list_repo_action_container);
        }

        public void bind(TestModel testModel) {
            mTextTitle.setText(testModel.title);
            mTextIndex.setText("#" + testModel.position);
            itemView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                        mItemTouchHelperExtension.startDrag(ItemBaseViewHolder.this);
                    }
                    return true;
                }
            });
        }
    }


    class ItemViewHolderWithRecyclerWidth extends ItemBaseViewHolder {

        View mActionViewDelete;

        public ItemViewHolderWithRecyclerWidth(View itemView) {
            super(itemView);
            mActionViewDelete = itemView.findViewById(R.id.view_list_repo_action_delete);
        }

    }

    class ItemSwipeWithActionWidthViewHolder extends ItemBaseViewHolder implements Extension {

        View mActionViewDelete;
        View mActionViewRefresh;

        public ItemSwipeWithActionWidthViewHolder(View itemView) {
            super(itemView);
            mActionViewDelete = itemView.findViewById(R.id.view_list_repo_action_delete);
            mActionViewRefresh = itemView.findViewById(R.id.view_list_repo_action_update);
        }

        @Override
        public float getActionWidth() {
            return mActionContainer.getWidth();
        }
    }

    class ItemSwipeWithActionWidthNoSpringViewHolder extends ItemSwipeWithActionWidthViewHolder implements Extension {

        public ItemSwipeWithActionWidthNoSpringViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public float getActionWidth() {
            return mActionContainer.getWidth();
        }
    }

    class ItemNoSwipeViewHolder extends ItemBaseViewHolder {

        public ItemNoSwipeViewHolder(View itemView) {
            super(itemView);
        }
    }

}
