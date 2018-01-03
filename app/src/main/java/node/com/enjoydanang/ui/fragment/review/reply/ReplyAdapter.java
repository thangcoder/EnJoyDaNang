package node.com.enjoydanang.ui.fragment.review.reply;

import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import node.com.enjoydanang.R;
import node.com.enjoydanang.model.PartnerAlbum;
import node.com.enjoydanang.model.Reply;
import node.com.enjoydanang.model.ReviewImage;
import node.com.enjoydanang.ui.fragment.review.ReviewAdapter;
import node.com.enjoydanang.utils.ImageUtils;
import node.com.enjoydanang.utils.Utils;
import node.com.enjoydanang.utils.event.OnItemClickListener;

/**
 * Author: Tavv
 * Created on 07/11/2017
 * Project Name: EnjoyDaNang
 * Version 1.0
 */

public class ReplyAdapter extends RecyclerView.Adapter<ReplyAdapter.ReplyViewHolder> {

    private List<Reply> replies;

    private int indexOfReview;

    private ImagePreviewAdapter.OnImageReviewClickListener onImageReviewClickListener;

    private ReviewAdapter.OnReplyClickListener onReplyClick;

    public ReplyAdapter(List<Reply> replies) {
        this.replies = replies;
    }


    public ReplyAdapter(List<Reply> replies, ImagePreviewAdapter.OnImageReviewClickListener onImageReviewClickListener,
                        ReviewAdapter.OnReplyClickListener onReplyClick, int indexOfReview) {
        this.replies = replies;
        this.onImageReviewClickListener = onImageReviewClickListener;
        this.indexOfReview = indexOfReview;
        this.onReplyClick = onReplyClick;
    }

    @Override
    public ReplyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_reply, parent, false);
        return new ReplyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReplyViewHolder holder, final int position) {
        final Reply reply = replies.get(position);
        ImageUtils.loadImageWithFreso(holder.imgAvatar, reply.getAvatar());
        holder.txtContentReview.setText(reply.getContent());
        holder.txtReviewerName.setText(reply.getName());
        int size = CollectionUtils.isEmpty(reply.getImages()) ? 0 : reply.getImages().size();
        String strCountImages = String.format(Locale.getDefault(), "%d %s", size, Utils.getLanguageByResId(R.string.Image));
        holder.txtNumberOfImages.setText(strCountImages);
        holder.txtNumberOfImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onImageReviewClickListener.onImageClick(view, position, reply.getAvatar(), getListImage(position));
            }
        });

        holder.txtRemoveReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onReplyClick.onClick(null, view, position, indexOfReview);
            }
        });
    }

    @Override
    public int getItemCount() {
        return CollectionUtils.isEmpty(replies) ? 0 : replies.size();
    }

    public static class ReplyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.imgAvatar)
        SimpleDraweeView imgAvatar;

        @BindView(R.id.txtReviewerName)
        TextView txtReviewerName;

        @BindView(R.id.txtContentReview)
        TextView txtContentReview;

        @BindView(R.id.txtRemoveReply)
        TextView txtRemoveReply;

        @BindView(R.id.txtNumberOfImages)
        TextView txtNumberOfImages;


        public ReplyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public Reply getItemAtPosition(int position) {
        return replies.get(position);
    }

    public void updateDataSource(List<Reply> lstReply) {
        if (CollectionUtils.isNotEmpty(replies)) {
            replies.clear();
        }
        replies.addAll(lstReply);
        notifyItemRangeChanged(0, replies.size());
        notifyDataSetChanged();
    }

    public List<Reply> getReplies() {
        return replies;
    }

    public void setReplies(List<Reply> replies) {
        this.replies = replies;
    }

    private ArrayList<PartnerAlbum> getListImage(int position) {
        ArrayList<PartnerAlbum> result = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(replies.get(position).getImages())) {
            int size = replies.get(position).getImages().size();
            for (int i = 0; i < size; i++) {
                ReviewImage image = replies.get(position).getImages().get(i);
                PartnerAlbum partnerAlbum = new PartnerAlbum();
                partnerAlbum.setTitle(StringUtils.EMPTY);
                partnerAlbum.setPicture(image.getPicture());
                result.add(partnerAlbum);
            }
        }
        return result;
    }

    public void setProgressMore(final boolean isProgress) {
        if (isProgress) {
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    replies.add(null);
                    notifyItemInserted(replies.size() - 1);
                }
            });
        } else {
            replies.remove(replies.size() - 1);
            notifyItemRemoved(replies.size());
        }
    }

    public void removeAt(int position) {
        replies.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, replies.size());
        notifyDataSetChanged();
    }
}
