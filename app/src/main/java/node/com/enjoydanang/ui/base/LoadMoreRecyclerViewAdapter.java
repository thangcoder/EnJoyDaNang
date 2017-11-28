package node.com.enjoydanang.ui.base;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import node.com.enjoydanang.R;
import node.com.enjoydanang.constant.Constant;

/**
 * Author: Tavv
 * Created on 28/11/2017.
 * Project Name: EnJoyDaNang
 * Version : 1.0
 */

public abstract class LoadMoreRecyclerViewAdapter<T> extends BaseRecyclerViewAdapter<T> {
    private static final int TYPE_PROGRESS = 0xFFFF;
    private boolean mOnLoadMoreFailed;
    private boolean mIsReachEnd;

    public LoadMoreRecyclerViewAdapter(@NonNull Context context,
                                       ItemClickListener itemClickListener) {
        super(context, itemClickListener);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_PROGRESS:
                View view = mInflater.inflate(R.layout.item_loading, parent, false);
                return new BottomViewHolder(view);
        }
        throw new RuntimeException("LoadMoreRecyclerViewAdapter: ViewHolder = null");
    }

    @Override
    public int getItemViewType(int position) {
        if (position == bottomItemPosition()) {
            return TYPE_PROGRESS;
        }
        return getCustomItemViewType(position);
    }

    private int bottomItemPosition() {
        return getItemCount() - 1;
    }

    protected abstract int getCustomItemViewType(int position);

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof BottomViewHolder) {
            if(mDataList.size() < Constant.DEFAULT_ITEM_EACH_FETCH && mIsReachEnd){
                ((BottomViewHolder) holder).mLinearLayout.setVisibility(View.GONE);
            }
            ((BottomViewHolder) holder).mProgressBar.setVisibility(
                    mIsReachEnd ? View.GONE : mOnLoadMoreFailed ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return mDataList.size() + 1; // +1 for progress
    }

    private static class BottomViewHolder extends RecyclerView.ViewHolder {
        private ProgressBar mProgressBar;
        private LinearLayout mLinearLayout;

        BottomViewHolder(View itemView) {
            super(itemView);
            mProgressBar = (ProgressBar) itemView.findViewById(R.id.progressLoading);
            mLinearLayout = (LinearLayout) itemView.findViewById(R.id.layout_loading);
        }
    }

    /**
     * It help visible progress when load more
     */
    public void startLoadMore() {
        mOnLoadMoreFailed = false;
        notifyDataSetChanged();
    }

    /**
     * It help visible layout retry when load more failed
     */
    public void onLoadMoreFailed() {
        mOnLoadMoreFailed = true;
        notifyItemChanged(bottomItemPosition());
    }

    public void onReachEnd() {
        mIsReachEnd = true;
        notifyItemChanged(bottomItemPosition());
    }
}