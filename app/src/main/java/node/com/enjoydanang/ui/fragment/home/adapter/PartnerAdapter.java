package node.com.enjoydanang.ui.fragment.home.adapter;

import android.content.Context;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import node.com.enjoydanang.R;
import node.com.enjoydanang.constant.Constant;
import node.com.enjoydanang.model.Partner;
import node.com.enjoydanang.utils.ImageUtils;
import node.com.enjoydanang.utils.event.OnItemClickListener;

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
    private OnItemClickListener mOnItemClickListener;

    private boolean isLoadingAdded = false;

    public PartnerAdapter(Context context, List<Partner> partners, OnItemClickListener mOnItemClickListener) {
        mContext = context;
        this.partners = partners;
        this.mOnItemClickListener = mOnItemClickListener;
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
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ViewHolder) {
            Partner partner = partners.get(position);
            ((ViewHolder) holder).tvTitle.setText(partner.getName());
            if (StringUtils.isNotBlank(partner.getDistance()) &&
                    !StringUtils.equals(partner.getDistance().trim(), "km") &&
                    partner.isDisplayDistance()) {
//                String distance = LanguageHelper.getValueByKey(Utils.getString(R.string.Partner_Distance)) + ": " + partner.getDistance() + "\t";
                String distance = partner.getDistance() + "\t";
                ((ViewHolder) holder).txtDistance.setText(distance);
            } else {
                ((ViewHolder) holder).txtDistance.setVisibility(View.GONE);
            }
            ImageUtils.loadImageWithFreso(((ViewHolder) holder).imgPhoto, partner.getPicture());
            boolean isFavorite = partner.getFavorite() > 0;
            ((ViewHolder) holder).fabFavorite.setImageResource(isFavorite ? R.drawable.follow : R.drawable.unfollow);
            ((ViewHolder) holder).mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnItemClickListener.onClick(view, position);
                }
            });
            ((ViewHolder) holder).fabFavorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnItemClickListener.onClick(view, position);
                }
            });
            int discount = partner.getDiscount();
            if (discount != 0) {
                String strDiscount = String.format(Locale.getDefault(), Constant.DISCOUNT_TEMPLATE, discount, "%");
                ((ViewHolder) holder).txtDiscount.setText(strDiscount);
                ((ViewHolder) holder).txtDiscount.setVisibility(View.VISIBLE);
            } else {
                ((ViewHolder) holder).txtDiscount.setVisibility(View.GONE);
            }
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
        return (partners.get(position) == null && isLoadingAdded) ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public final View mView;
        @BindView(R.id.img_partner_photo)
        SimpleDraweeView imgPhoto;
        @BindView(R.id.tv_partner_name)
        TextView tvTitle;
        @BindView(R.id.txtDistance)
        TextView txtDistance;
        @BindView(R.id.fabFavorite)
        FloatingActionButton fabFavorite;

        @BindView(R.id.txtDiscount)
        TextView txtDiscount;

        public ViewHolder(View v) {
            super(v);
            mView = v;
            ButterKnife.bind(this, mView);
        }
    }

    public void clearAndUpdateData(List<Partner> partners) {
        if (this.partners != null && this.partners.size() > 0) {
            clear();
        }
        if (this.partners != null) {
            this.partners.addAll(partners);
        }
        notifyDataSetChanged();
    }

    private void clearData() {
        int size = this.partners.size();
        this.partners.clear();
        notifyItemRangeRemoved(0, size);
    }


    public void setProgressMore(final boolean isProgress) {
        if (isProgress) {
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    partners.add(null);
                    notifyItemInserted(partners.size() - 1);
                }
            });
        } else {
            partners.remove(partners.size() - 1);
            notifyItemRemoved(partners.size());
        }
    }


    public void add(Partner partner) {
        partners.add(partner);
        notifyItemInserted(partners.size() - 1);
    }

    public void addAll(List<Partner> mcList) {
        for (Partner mc : mcList) {
            add(mc);
        }
    }

    public void remove(Partner partner) {
        int position = partners.indexOf(partner);
        if (position > -1) {
            partners.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        isLoadingAdded = false;
        while (getItemCount() > 0) {
            remove(getItem(0));
        }
    }

    public boolean isEmpty() {
        return getItemCount() == 0;
    }


    public void addLoadingFooter() {
        isLoadingAdded = true;
        add(null);
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;
        int position = partners.size() - 1;
        Partner item = getItem(position);
        if (item != null) {
            partners.remove(position);
            notifyItemRemoved(position);
        }
    }

    public Partner getItem(int position) {
        return partners.get(position);
    }

}