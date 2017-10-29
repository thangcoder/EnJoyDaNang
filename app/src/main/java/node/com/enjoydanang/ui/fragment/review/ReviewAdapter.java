package node.com.enjoydanang.ui.fragment.review;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import node.com.enjoydanang.R;
import node.com.enjoydanang.constant.Constant;
import node.com.enjoydanang.model.Partner;
import node.com.enjoydanang.model.Review;
import node.com.enjoydanang.utils.ImageUtils;
import node.com.enjoydanang.utils.Utils;
import node.com.enjoydanang.utils.event.OnItemClickListener;

import static android.R.attr.animation;

/**
 * Author: Tavv
 * Created on 11/10/2017.
 * Project Name: EnJoyDaNang
 * Version : 1.0
 */

public class ReviewAdapter extends RecyclerView.Adapter {

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;

    private List<Review> lstReviews;
    private Context context;
    private OnItemClickListener onItemClickListener;
    private boolean isExpand;

    public ReviewAdapter(Context context, List<Review> lstReviews, OnItemClickListener onItemClickListener) {
        this.context = context;
        this.lstReviews = lstReviews;
        this.onItemClickListener = onItemClickListener;
    }

    public class ReviewViewHolder extends RecyclerView.ViewHolder {
        CircleImageView imgAvatar;
        TextView txtReviewerName;
        TextView txtNumRate;
        TextView txtContentReview;
        TextView txtTitleReview;
        TextView txtDate;
        ImageView imgExpanCollapseContent;
        ImageView imgReply;

        ReviewViewHolder(View itemView) {
            super(itemView);
            imgAvatar = (CircleImageView) itemView.findViewById(R.id.imgAvatar);
            txtReviewerName = (TextView) itemView.findViewById(R.id.txtReviewerName);
            txtNumRate = (TextView) itemView.findViewById(R.id.txtNumRate);
            txtContentReview = (TextView) itemView.findViewById(R.id.txtContentReview);
            txtTitleReview = (TextView) itemView.findViewById(R.id.txtTitleReview);
            txtDate = (TextView) itemView.findViewById(R.id.txtDate);
            imgExpanCollapseContent = (ImageView) itemView.findViewById(R.id.imgExpanCollapseContent);
            imgReply = (ImageView) itemView.findViewById(R.id.btnReply);
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
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ReviewViewHolder) {
            Review model = lstReviews.get(position);
            ((ReviewViewHolder) holder).txtContentReview.setText(model.getContent());
            ((ReviewViewHolder) holder).txtNumRate.setText(String.valueOf(model.getStar()));
            ((ReviewViewHolder) holder).txtReviewerName.setText(model.getName());
            ((ReviewViewHolder) holder).txtTitleReview.setText(model.getTitle());
            ((ReviewViewHolder) holder).txtDate.setText(Utils.formatDate(Constant.DATE_SERVER_FORMAT, Constant.DATE_FORMAT_DMY, model.getDate()));
            if (((ReviewViewHolder) holder).txtContentReview.getLineCount() > 3) {
                ((ReviewViewHolder) holder).imgExpanCollapseContent.setVisibility(View.VISIBLE);
                ((ReviewViewHolder) holder).imgExpanCollapseContent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(isExpand){
                            expand(((ReviewViewHolder) holder).txtContentReview, false);
                            ((ReviewViewHolder) holder).imgExpanCollapseContent.setImageResource(R.drawable.ic_keyboard_arrow_up_black_24dp);
                        }else{
                            expand(((ReviewViewHolder) holder).txtContentReview, true);
                            ((ReviewViewHolder) holder).imgExpanCollapseContent.setImageResource(R.drawable.ic_keyboard_arrow_down_black_24dp);
                        }
                    }
                });
            } else {
                ((ReviewViewHolder) holder).imgExpanCollapseContent.setVisibility(View.INVISIBLE);
            }
            ((ReviewViewHolder) holder).imgReply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickListener.onClick(view, position);
                }
            });
//            ImageUtils.loadImageNoRadius(context, ((ReviewViewHolder) holder).imgAvatar, model.getAvatar());
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


    private void expand(TextView textView, boolean collapse) {
        ObjectAnimator animation = null;
        if (collapse) {
            isExpand = true;
            animation= ObjectAnimator.ofInt(
                    textView,
                    "maxLines",
                    10);
        } else {
            isExpand = false;
            animation = ObjectAnimator.ofInt(
                    textView,
                    "maxLines",
                    3);
        }
        animation.setDuration(500);
        animation.start();
    }
}
