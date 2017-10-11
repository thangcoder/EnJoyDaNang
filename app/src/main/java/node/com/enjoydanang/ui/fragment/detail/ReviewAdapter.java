package node.com.enjoydanang.ui.fragment.detail;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import node.com.enjoydanang.R;
import node.com.enjoydanang.model.ItemReviewModel;

/**
 * Author: Tavv
 * Created on 11/10/2017.
 * Project Name: EnJoyDaNang
 * Version : 1.0
 */

public class ReviewAdapter extends RecyclerView.Adapter {

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;

    private List<ItemReviewModel> lstReviews;
    private Context context;

    public ReviewAdapter(Context context, List<ItemReviewModel> lstReviews) {
        this.context = context;
        this.lstReviews = lstReviews;
    }

    public class ReviewViewHolder extends RecyclerView.ViewHolder {
        CircleImageView imgAvatar;
        TextView txtReviewerName;
        TextView txtNumRate;
        TextView txtContentReview;

        ReviewViewHolder(View itemView) {
            super(itemView);
            imgAvatar = (CircleImageView) itemView.findViewById(R.id.imgAvatar);
            txtReviewerName = (TextView) itemView.findViewById(R.id.txtReviewerName);
            txtNumRate = (TextView) itemView.findViewById(R.id.txtNumRate);
            txtContentReview = (TextView) itemView.findViewById(R.id.txtContentReview);
        }
    }

    public class LoadingViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public LoadingViewHolder(View itemView) {
            super(itemView);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progressLoading);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_review, parent, false);
            viewHolder = new ReviewViewHolder(view);
        } else if (viewType == VIEW_TYPE_LOADING) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_loading, parent, false);
            viewHolder = new LoadingViewHolder(view);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ReviewViewHolder) {
            ItemReviewModel model = lstReviews.get(position);
            Glide.with(context).load(model.getAvatar())
//                        .placeholder(R.drawable.bg_card_top_music_china)
                    .into(((ReviewViewHolder) holder).imgAvatar);
            ((ReviewViewHolder) holder).txtContentReview.setText(model.getComment());
            ((ReviewViewHolder) holder).txtNumRate.setText(String.valueOf(model.getRate()));
            ((ReviewViewHolder) holder).txtReviewerName.setText(model.getReviewerName());

        } else if (holder instanceof LoadingViewHolder) {
            ((LoadingViewHolder) holder).progressBar.setIndeterminate(true);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return lstReviews.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        return lstReviews.size() > 0 ? lstReviews.size() : 0;
    }


}
