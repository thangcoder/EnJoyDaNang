package node.com.enjoydanang.ui.fragment.detail;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Locale;

import node.com.enjoydanang.R;
import node.com.enjoydanang.constant.Constant;
import node.com.enjoydanang.model.Partner;
import node.com.enjoydanang.utils.ImageUtils;
import node.com.enjoydanang.utils.event.OnItemClickListener;

/**
 * Author: Tavv
 * Created on 16/12/2017
 * Project Name: EnjoyDaNang
 * Version 1.0
 */

public class PartnerAroundAdapter extends RecyclerView.Adapter<PartnerAroundAdapter.PartnerAroundVH> {

    private static final int IMAGE_WIDTH = 350;
    private static final int IMAGE_HEIGHT = 250;

    private List<Partner> lstPartner;

    private OnItemClickListener onItemClickListener;

    public PartnerAroundAdapter(List<Partner> lstPartner, OnItemClickListener onItemClickListener) {
        this.lstPartner = lstPartner;
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public PartnerAroundVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_partner_around, parent, false);
        return new PartnerAroundVH(view);
    }

    @Override
    public void onBindViewHolder(PartnerAroundVH holder, final int position) {
        if (CollectionUtils.isNotEmpty(lstPartner)) {
            Partner partner = lstPartner.get(position);
            if (partner != null) {
                holder.txtPartnerName.setText(partner.getName());
                if (partner.getDiscount() == 0) {
                    holder.txtDiscountNear.setVisibility(View.INVISIBLE);
                } else {
                    String strDiscount = String.format(Locale.getDefault(), Constant.DISCOUNT_TEMPLATE, partner.getDiscount(), "%");
                    holder.txtDiscountNear.setText(strDiscount);
                    holder.txtDiscountNear.setVisibility(View.VISIBLE);
                }
                if (StringUtils.isNotBlank(partner.getDistance()) && partner.isDisplayDistance()) {
                    holder.txtDistance.setText(partner.getDistance());
                    holder.txtDistance.setVisibility(View.VISIBLE);
                } else {
                    holder.txtDistance.setVisibility(View.INVISIBLE);
                }
                ImageUtils.loadImageWithScaleFreso(holder.imgPartner, partner.getPicture(), IMAGE_WIDTH, IMAGE_HEIGHT);
                holder.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onItemClickListener.onClick(view, position);
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        return CollectionUtils.isEmpty(lstPartner) ? 0 : lstPartner.size();
    }

    static class PartnerAroundVH extends RecyclerView.ViewHolder {

        SimpleDraweeView imgPartner;

        TextView txtPartnerName;

        TextView txtDiscountNear;

        TextView txtDistance;

        View view;

        public PartnerAroundVH(View itemView) {
            super(itemView);
            view = itemView;
            imgPartner = (SimpleDraweeView) itemView.findViewById(R.id.imgPartner);
            txtPartnerName = (TextView) itemView.findViewById(R.id.txtPartnerName);
            txtDistance = (TextView) itemView.findViewById(R.id.txtDistance);
            txtDiscountNear = (TextView) itemView.findViewById(R.id.txtDiscountNear);
        }
    }
}
