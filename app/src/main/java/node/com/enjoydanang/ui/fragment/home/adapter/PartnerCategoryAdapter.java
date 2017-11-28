package node.com.enjoydanang.ui.fragment.home.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import node.com.enjoydanang.R;
import node.com.enjoydanang.constant.Constant;
import node.com.enjoydanang.model.Partner;
import node.com.enjoydanang.ui.base.LoadMoreRecyclerViewAdapter;
import node.com.enjoydanang.utils.ImageUtils;

/**
 * Author: Tavv
 * Created on 28/11/2017.
 * Project Name: EnJoyDaNang
 * Version : 1.0
 */

public class PartnerCategoryAdapter extends LoadMoreRecyclerViewAdapter<Partner> {
    private static final int TYPE_ITEM = 1;

    public PartnerCategoryAdapter(@NonNull Context context, ItemClickListener itemClickListener) {
        super(context, itemClickListener);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_partner, parent, false);
            return new PartnerCategoryAdapter.PartnerViewHolder(view);
        }
        return super.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof PartnerViewHolder) {
            Partner partner = mDataList.get(position);
            ((PartnerViewHolder) holder).tvTitle.setText(partner.getName());
            ImageUtils.loadImageWithFreso(((PartnerViewHolder) holder).imgPhoto, partner.getPicture());
            boolean isFavorite = partner.getFavorite() > 0;
            ((PartnerViewHolder) holder).fabFavorite.setImageResource(isFavorite ? R.drawable.follow : R.drawable.unfollow);
            ((PartnerViewHolder) holder).mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mItemClickListener.onItemClick(view, position);
                }
            });
            ((PartnerViewHolder) holder).fabFavorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mItemClickListener.onItemClick(view, position);
                }
            });
            int discount = partner.getDiscount();
            if (discount != 0) {
                String strDiscount = String.format(Locale.getDefault(), Constant.DISCOUNT_TEMPLATE, discount, "%");
                ((PartnerViewHolder) holder).txtDiscount.setText(strDiscount);
                ((PartnerViewHolder) holder).txtDiscount.setVisibility(View.VISIBLE);
            } else {
                ((PartnerViewHolder) holder).txtDiscount.setVisibility(View.GONE);
            }
        }
        super.onBindViewHolder(holder, position);
    }

    @Override
    protected int getCustomItemViewType(int position) {
        return TYPE_ITEM;
    }


    public static class PartnerViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public final View mView;
        @BindView(R.id.img_partner_photo)
        SimpleDraweeView imgPhoto;
        @BindView(R.id.tv_partner_name)
        TextView tvTitle;
        @BindView(R.id.fabFavorite)
        FloatingActionButton fabFavorite;

        @BindView(R.id.txtDiscount)
        TextView txtDiscount;

        public PartnerViewHolder(View v) {
            super(v);
            mView = v;
            ButterKnife.bind(this, mView);
        }
    }
}
