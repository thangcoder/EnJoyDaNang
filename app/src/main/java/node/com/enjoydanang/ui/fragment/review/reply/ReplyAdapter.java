package node.com.enjoydanang.ui.fragment.review.reply;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import org.apache.commons.collections.CollectionUtils;

import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import node.com.enjoydanang.R;
import node.com.enjoydanang.model.Reply;
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

    private OnItemClickListener onItemClickListener;

    public ReplyAdapter(List<Reply> replies) {
        this.replies = replies;
    }

    public ReplyAdapter(List<Reply> replies, OnItemClickListener onItemClickListener) {
        this.replies = replies;
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public ReplyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_reply, parent, false);
        return new ReplyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReplyViewHolder holder, final int position) {
        Reply reply = replies.get(position);
        ImageUtils.loadImageWithFreso(holder.imgAvatar, reply.getAvatar());
        holder.txtContentReview.setText(reply.getContent());
        holder.txtReviewerName.setText(reply.getUserName());
        int size = CollectionUtils.isEmpty(reply.getImages()) ? 0 : reply.getImages().size();
        String strCountImages = String.format(Locale.getDefault(), "%d %s", size, Utils.getLanguageByResId(R.string.Image));
        holder.txtNumberOfImages.setText(strCountImages);
        holder.txtNumberOfImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.onClick(view, position);
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

        @BindView(R.id.txtNumberOfImages)
        TextView txtNumberOfImages;


        public ReplyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public Reply getItemAtPosition(int position){
        return replies.get(position);
    }

    public void updateDataSource(List<Reply> lstReply){
        if(CollectionUtils.isNotEmpty(replies)){
            replies.clear();
        }
        replies.addAll(lstReply);
        notifyItemRangeChanged(0, replies.size());
        notifyDataSetChanged();
    }
}
