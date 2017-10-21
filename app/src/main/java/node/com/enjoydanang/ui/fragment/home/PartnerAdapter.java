package node.com.enjoydanang.ui.fragment.home;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import node.com.enjoydanang.R;
import node.com.enjoydanang.model.Partner;
import node.com.enjoydanang.model.Product;
import node.com.enjoydanang.utils.ImageUtils;

/**
 * Author: Tavv
 * Created on 21/10/2017
 * Project Name: EnjoyDaNang
 * Version 1.0
 */

public class PartnerAdapter extends RecyclerView.Adapter {
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;

    private List<Partner> partners;
    private Context mContext;

    public PartnerAdapter(Context context, List<Partner> partners) {
        mContext = context;
        this.partners = partners;
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
                    .inflate(R.layout.item_partner, parent, false);
            viewHolder = new ViewHolder(view);
        } else if (viewType == VIEW_TYPE_LOADING) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_loading, parent, false);
            viewHolder = new LoadingViewHolder(view);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof ViewHolder) {
            Partner partner = partners.get(position);
            ((ViewHolder) holder).tvTitle.setText(partner.getName());
            //TODO : remove HardCode Meta In Partner Adapter
            ((ViewHolder) holder).tvMeta.setText("Jenky");
            ImageUtils.loadImageNoRadius(mContext, ((ViewHolder) holder).imgPhoto, partner.getPicture());
        } else if (holder instanceof LoadingViewHolder) {
            ((LoadingViewHolder) holder).progressBar.setIndeterminate(true);
        }
    }


    @Override
    public int getItemCount() {
        return partners.size() > 0 ? partners.size() : 0;
    }


    @Override
    public int getItemViewType(int position) {
        return partners.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
//        public SimpleDraweeView imgPhoto;
        public final View mView;
        @BindView(R.id.img_partner_photo)
        ImageView imgPhoto;
        @BindView(R.id.tv_partner_name)
        TextView tvTitle;
        @BindView(R.id.tv_partner_meta)
        TextView tvMeta;

        public ViewHolder(View v) {
            super(v);
            mView = v;
            ButterKnife.bind(this, mView);
        }
    }

}