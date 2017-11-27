package node.com.enjoydanang.utils.helper;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * Author: Tavv
 * Created on 27/11/2017.
 * Project Name: EnJoyDaNang
 * Version : 1.0
 */

public abstract class EndlessScrollListener extends RecyclerView.OnScrollListener {

    private int previousTotal = 0;
    private boolean isLoading = true;
    int firstVisibleItem, childCount, itemCount;

    private int currentPage = 0;

    private LinearLayoutManager mLinearLayoutManager;

    public EndlessScrollListener(LinearLayoutManager linearLayoutManager) {
        this.mLinearLayoutManager = linearLayoutManager;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        childCount = recyclerView.getChildCount();
        itemCount = mLinearLayoutManager.getItemCount();
        firstVisibleItem = mLinearLayoutManager.findFirstVisibleItemPosition();

        if (isLoading) {
            if (itemCount > previousTotal) {
                isLoading = false;
                previousTotal = itemCount;
            }
        }
        if (!isLoading && (itemCount - childCount) <= firstVisibleItem) {
            currentPage++;
            onLoadMore(currentPage);
            isLoading = true;
        }
    }

    public abstract void onLoadMore(int currentPage);
}