package node.com.enjoydanang.ui.fragment.review;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.github.aakira.expandablelayout.ExpandableLayout;
import com.github.aakira.expandablelayout.ExpandableLayoutListenerAdapter;
import com.github.aakira.expandablelayout.ExpandableRelativeLayout;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import node.com.enjoydanang.R;
import node.com.enjoydanang.constant.Constant;
import node.com.enjoydanang.model.ImageData;
import node.com.enjoydanang.model.Reply;
import node.com.enjoydanang.model.Review;
import node.com.enjoydanang.model.ReviewImage;
import node.com.enjoydanang.ui.fragment.review.reply.ImagePreviewAdapter;
import node.com.enjoydanang.ui.fragment.review.reply.ReplyAdapter;
import node.com.enjoydanang.utils.ImageUtils;
import node.com.enjoydanang.utils.Utils;
import node.com.enjoydanang.utils.event.OnItemClickListener;
import node.com.enjoydanang.utils.helper.LanguageHelper;
import node.com.enjoydanang.utils.widget.BetterRecyclerView;
import node.com.enjoydanang.utils.widget.DividerItemDecoration;

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
    private List<List<Reply>> lstReply;
    private Context context;
    private OnItemClickListener onItemClickListener;

    private ImagePreviewAdapter.OnImageReviewClickListener onImagePreviewClick;

    public interface OnReplyClickListener {
        void onClick(ProgressBar prgLoading, View view, int position);
    }

    private OnReplyClickListener onReplyClickListener;

    public ReviewAdapter(Context context, List<Review> lstReviews, OnItemClickListener onItemClickListener) {
        this.context = context;
        this.lstReviews = lstReviews;
        this.onItemClickListener = onItemClickListener;
    }

    public ReviewAdapter(List<Review> lstReviews, List<List<Reply>> lstReply,
                         Context context, OnItemClickListener onItemClickListener,
                         ImagePreviewAdapter.OnImageReviewClickListener onImagePreviewClick,
                         OnReplyClickListener onReplyClickListener) {
        this.lstReviews = lstReviews;
        this.context = context;
        this.onItemClickListener = onItemClickListener;
        this.onImagePreviewClick = onImagePreviewClick;
        this.onReplyClickListener = onReplyClickListener;
        this.lstReply = lstReply;
    }

    public static class ReviewViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.imgAvatar)
        SimpleDraweeView imgAvatar;

        @BindView(R.id.txtReviewerName)
        TextView txtReviewerName;

        @BindView(R.id.txtNumRate)
        TextView txtNumRate;

        @BindView(R.id.txtContentReview)
        TextView txtContentReview;

        @BindView(R.id.txtTitleReview)
        TextView txtTitleReview;

        @BindView(R.id.txtDate)
        TextView txtDate;

        @BindView(R.id.imgExpanCollapseContent)
        ImageView imgExpanCollapseContent;

        @BindView(R.id.btnReply)
        ImageView imgReply;

        @BindView(R.id.rcvImageReview)
        BetterRecyclerView rcvImageReview;

        @BindView(R.id.expandableReview)
        ExpandableRelativeLayout expandableLayout;

        @BindView(R.id.rcvReply)
        RecyclerView rcvReply;

        @BindView(R.id.prgLoadingReply)
        ProgressBar prgLoadingReply;

        @BindView(R.id.txtWriteReply)
        TextView txtWriteReply;


        ReviewViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    static class LoadingViewHolder extends RecyclerView.ViewHolder {
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
            final Review model = lstReviews.get(position);
            if (StringUtils.isNotBlank(model.getContent())) {
                ((ReviewViewHolder) holder).txtContentReview.setText(model.getContent());
            } else {
                ((ReviewViewHolder) holder).txtContentReview.setVisibility(View.GONE);
            }
            ((ReviewViewHolder) holder).txtNumRate.setText(String.valueOf(model.getStar()));
            ((ReviewViewHolder) holder).txtReviewerName.setText(model.getName());
            ((ReviewViewHolder) holder).txtTitleReview.setText(model.getTitle());
            ((ReviewViewHolder) holder).txtDate.setText(Utils.formatDate(Constant.DATE_SERVER_FORMAT, Constant.DATE_FORMAT_DMY, model.getDate()));
            LanguageHelper.getValueByViewId(((ReviewViewHolder) holder).txtWriteReply);
            ImageUtils.loadImageWithFreso(((ReviewViewHolder) holder).imgAvatar, model.getAvatar());
            if (((ReviewViewHolder) holder).txtContentReview.getLineCount() > 3) {
                ((ReviewViewHolder) holder).imgExpanCollapseContent.setVisibility(View.VISIBLE);
                ((ReviewViewHolder) holder).imgExpanCollapseContent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (model.isExpanded()) {
                            model.setExpanded(false);
                            expand(((ReviewViewHolder) holder).txtContentReview, false);
                            ((ReviewViewHolder) holder).imgExpanCollapseContent.setImageResource(R.drawable.ic_keyboard_arrow_down_black_24dp);
                        } else {
                            model.setExpanded(true);
                            expand(((ReviewViewHolder) holder).txtContentReview, true);
                            ((ReviewViewHolder) holder).imgExpanCollapseContent.setImageResource(R.drawable.ic_keyboard_arrow_up_black_24dp);
                        }
                    }
                });
            } else {
                ((ReviewViewHolder) holder).imgExpanCollapseContent.setVisibility(View.GONE);
            }

            //List images preview after choose
            final List<ImageData> images = new ArrayList<>();
            for (ReviewImage reviewImage : model.getImages()) {
                images.add(new ImageData(null, null, reviewImage.getPicture()));
            }
            if (CollectionUtils.isNotEmpty(images)) {
                initAdapter(((ReviewViewHolder) holder).rcvImageReview, LinearLayoutManager.HORIZONTAL);
                ImagePreviewAdapter reviewAdapter = new ImagePreviewAdapter(images, context, onImagePreviewClick);
                ((ReviewViewHolder) holder).rcvImageReview.setAdapter(reviewAdapter);
            } else {
                ((ReviewViewHolder) holder).rcvImageReview.setVisibility(View.GONE);
            }
            // Reply
            initAdapter(((ReviewViewHolder) holder).rcvReply, LinearLayoutManager.VERTICAL);
            lstReply.add(new ArrayList<Reply>());
            ReplyAdapter replyAdapter = new ReplyAdapter(lstReply.get(position), onImagePreviewClick);
            ((ReviewViewHolder) holder).rcvReply.addItemDecoration(new DividerItemDecoration(context, LinearLayoutManager.VERTICAL));
            ((ReviewViewHolder) holder).rcvReply.setAdapter(replyAdapter);
            initExpandableLayout(holder, model);
            ((ReviewViewHolder) holder).imgReply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!model.isExpandedComment()) {
                        onReplyClickListener.onClick((((ReviewViewHolder) holder).prgLoadingReply), view, position);
                    }
                    onClickButton(((ReviewViewHolder) holder).expandableLayout);
                }
            });
            ((ReviewViewHolder) holder).txtWriteReply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickListener.onClick(view, position);
                }
            });
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
            animation = ObjectAnimator.ofInt(
                    textView,
                    "maxLines",
                    10);
        } else {
            animation = ObjectAnimator.ofInt(
                    textView,
                    "maxLines",
                    3);
        }
        animation.setDuration(500);
        animation.start();
    }

    private <T extends RecyclerView> void initAdapter(T recyclerView, int orientation) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, orientation, false);
        recyclerView.setLayoutManager(layoutManager);
    }

    private void onClickButton(final ExpandableLayout expandableLayout) {
        expandableLayout.toggle();
    }

    private void initExpandableLayout(RecyclerView.ViewHolder holder, final Review review) {
        holder.setIsRecyclable(false);
        ((ReviewViewHolder) holder).expandableLayout.setExpanded(false);
        ((ReviewViewHolder) holder).expandableLayout.setInterpolator(review.getInterpolator());
        if (!review.isExpandedComment()) {
            ((ReviewViewHolder) holder).expandableLayout.collapse();
        }
        ((ReviewViewHolder) holder).expandableLayout.setListener(new ExpandableLayoutListenerAdapter() {
            @Override
            public void onPreOpen() {
                review.setExpandedComment(true);
            }

            @Override
            public void onPreClose() {
                review.setExpandedComment(false);
            }
        });
    }

    public void setProgressMore(final boolean isProgress) {
        if (isProgress) {
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    lstReviews.add(null);
                    notifyItemInserted(lstReviews.size() - 1);
                }
            });
        } else {
            lstReviews.remove(lstReviews.size() - 1);
            notifyItemRemoved(lstReviews.size());
        }
    }


}
