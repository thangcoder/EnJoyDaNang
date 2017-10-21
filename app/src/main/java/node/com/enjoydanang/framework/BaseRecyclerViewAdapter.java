package node.com.enjoydanang.framework;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: Tavv
 * Created on 21/10/2017
 * Project Name: EnjoyDaNang
 * Version 1.0
 */

public abstract class BaseRecyclerViewAdapter <T>
        extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    protected LayoutInflater mInflater;
    protected List<T> mDataList;
    protected ItemClickListener mItemClickListener;

    protected BaseRecyclerViewAdapter(@NonNull Context context,
                                      ItemClickListener itemClickListener) {
        mInflater = LayoutInflater.from(context);
        mItemClickListener = itemClickListener;
        mDataList = new ArrayList<>();
    }

    public void add(List<T> itemList) {
        mDataList.addAll(itemList);
        notifyDataSetChanged();
    }

    public void set(List<T> dataList) {
        List<T> clone = new ArrayList<>(dataList);
        mDataList.clear();
        mDataList.addAll(clone);
        notifyDataSetChanged();
    }

    public void clear() {
        mDataList.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}