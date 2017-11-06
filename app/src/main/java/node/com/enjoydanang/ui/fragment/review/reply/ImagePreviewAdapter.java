package node.com.enjoydanang.ui.fragment.review.reply;

import android.content.Context;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import org.apache.commons.collections.CollectionUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import node.com.enjoydanang.R;
import node.com.enjoydanang.model.ImageData;
import node.com.enjoydanang.utils.ImageUtils;

/**
 * Author: Tavv
 * Created on 06/11/2017
 * Project Name: EnjoyDaNang
 * Version 1.0
 */

public class ImagePreviewAdapter extends RecyclerView.Adapter<ImagePreviewAdapter.ImagePreviewHolder> {

    private Context context;

    private List<ImageData> images;

    public ImagePreviewAdapter(List<ImageData> images, Context context) {
        this.images = images;
        this.context = context;
    }

    @Override
    public ImagePreviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image_preview, parent, false);
        return new ImagePreviewHolder(view);
    }

    @Override
    public void onBindViewHolder(ImagePreviewHolder holder, final int position) {
        final ImageData image = images.get(position);
        ImageUtils.loadImageFromUri(context, holder.imgPreview, image.getUri());
        holder.removeChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeItem(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return CollectionUtils.isEmpty(images) ? 0 : images.size();
    }

    public class ImagePreviewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.imgPreview)
        ImageView imgPreview;

        @BindView(R.id.removeChoose)
        ImageView removeChoose;

        public ImagePreviewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void removeItem(int position) {
        if (CollectionUtils.isNotEmpty(images)) {
            images.remove(position);
            int size = images.size();
            notifyItemRangeRemoved(0, size);
            notifyDataSetChanged();
        }
    }

}
