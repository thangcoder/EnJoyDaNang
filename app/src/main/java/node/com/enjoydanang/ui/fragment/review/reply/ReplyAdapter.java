package node.com.enjoydanang.ui.fragment.review.reply;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import org.apache.commons.collections.CollectionUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import node.com.enjoydanang.R;
import node.com.enjoydanang.model.Reply;
import node.com.enjoydanang.utils.ImageUtils;

/**
 * Author: Tavv
 * Created on 07/11/2017
 * Project Name: EnjoyDaNang
 * Version 1.0
 */

public class ReplyAdapter extends RecyclerView.Adapter<ReplyAdapter.ReplyViewHolder> {

    private Context context;

    private List<Reply> replies;

    public ReplyAdapter(Context context, List<Reply> replies) {
        this.context = context;
        this.replies = replies;
    }

    @Override
    public ReplyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_reply, parent, false);
        return new ReplyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReplyViewHolder holder, int position) {
        Reply reply = replies.get(position);
        ImageUtils.loadImageWithFreso(holder.imgAvatar, reply.getAvatar());
        holder.txtContentReview.setText(reply.getContent());
        holder.txtReviewerName.setText(reply.getUserName());
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


        public ReplyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
