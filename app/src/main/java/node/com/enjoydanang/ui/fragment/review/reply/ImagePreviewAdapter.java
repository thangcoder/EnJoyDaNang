package node.com.enjoydanang.ui.fragment.review.reply;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import node.com.enjoydanang.R;
import node.com.enjoydanang.model.ImageData;
import node.com.enjoydanang.model.PartnerAlbum;
import node.com.enjoydanang.utils.ImageUtils;

/**
 * Author: Tavv
 * Created on 06/11/2017
 * Project Name: EnjoyDaNang
 * Version 1.0
 */

public class ImagePreviewAdapter extends RecyclerView.Adapter<ImagePreviewAdapter.ImagePreviewHolder> {

    private static final int SIZE_IMAGE_THUMB = 120;

    private static final int SIZE_IMAGE_PREVIEW = 120;

    private Context context;

    private List<ImageData> images;

    private int IMAGE_SIZE;

    public interface OnImageReviewClickListener {
        void onImageClick(View view, int position, String url, ArrayList<PartnerAlbum> images);
    }

    private OnImageReviewClickListener onImageReviewClickListener;

    public ImagePreviewAdapter(List<ImageData> images, Context context) {
        this.images = images;
        this.context = context;
    }

    public ImagePreviewAdapter(List<ImageData> images, Context context, final int IMAGE_SIZE) {
        this.images = images;
        this.context = context;
        this.IMAGE_SIZE = IMAGE_SIZE;
    }

    public ImagePreviewAdapter(List<ImageData> images, Context context, OnImageReviewClickListener onImageReviewClickListener) {
        this.context = context;
        this.images = images;
        this.onImageReviewClickListener = onImageReviewClickListener;
    }

    public ImagePreviewAdapter(List<ImageData> images, Context context, final int IMAGE_SIZE, OnImageReviewClickListener onImageReviewClickListener) {
        this.images = images;
        this.context = context;
        this.IMAGE_SIZE = IMAGE_SIZE;
        this.onImageReviewClickListener = onImageReviewClickListener;
    }

    @Override
    public ImagePreviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image_preview, parent, false);
        return new ImagePreviewHolder(view);
    }

    @Override
    public void onBindViewHolder(ImagePreviewHolder holder, final int position) {
        final ImageData image = images.get(position);
        if (image.getUri() != null) {
            int size = IMAGE_SIZE == 0 ? SIZE_IMAGE_PREVIEW : IMAGE_SIZE;
            setSizeImage(holder.imgPreview, size, size);
            ImageUtils.loadResizeImage(context, holder.imgPreview, image.getUri(), size, size);
            holder.removeChoose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    removeItem(position);
                }
            });
        } else if (StringUtils.isNotBlank(image.getUrl())) {
            setSizeImage(holder.imgPreview, SIZE_IMAGE_THUMB, SIZE_IMAGE_THUMB);
            ImageUtils.loadResizeImage(context, holder.imgPreview, image.getUrl(), SIZE_IMAGE_THUMB, SIZE_IMAGE_THUMB);
            holder.removeChoose.setVisibility(View.GONE);
            holder.imgPreview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onImageReviewClickListener.onImageClick(view, position, image.getUrl(), getListImage());
                }
            });
        }
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

    private ArrayList<PartnerAlbum> getListImage() {
        ArrayList<PartnerAlbum> result = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(images)) {
            int size = images.size();
            for (int i = 0; i < size; i++) {
                PartnerAlbum partnerAlbum = new PartnerAlbum();
                partnerAlbum.setTitle(StringUtils.EMPTY);
                partnerAlbum.setPicture(images.get(i).getUrl());
                result.add(partnerAlbum);
            }
        }
        return result;
    }

    public List<ImageData> getImages() {
        return images;
    }

    private void setSizeImage(ImageView imageView, int width, int height){
        ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
        layoutParams.width = width;
        layoutParams.height = height;
        imageView.setLayoutParams(layoutParams);
    }
}
