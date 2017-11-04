package node.com.enjoydanang.ui.fragment.logcheckin;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import node.com.enjoydanang.R;
import node.com.enjoydanang.constant.Constant;
import node.com.enjoydanang.model.HistoryCheckin;
import node.com.enjoydanang.utils.ImageUtils;
import node.com.enjoydanang.utils.Utils;
import node.com.enjoydanang.utils.event.OnItemClickListener;

/**
 * Author: Tavv
 * Created on 30/10/2017
 * Project Name: EnjoyDaNang
 * Version 1.0
 */

public class CheckinHistoryAdapter extends RecyclerView.Adapter<CheckinHistoryAdapter.CheckinViewHolder> {

    private List<HistoryCheckin> lstHistoryCheckin;
    private Context context;
    private OnItemClickListener onItemClickListener;

    public CheckinHistoryAdapter(List<HistoryCheckin> lstHistoryCheckin, Context context, OnItemClickListener onItemClickListener) {
        this.lstHistoryCheckin = lstHistoryCheckin;
        this.context = context;
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public CheckinViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_log_check_in, parent, false);
        return new CheckinViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CheckinViewHolder holder, final int position) {
        HistoryCheckin model = lstHistoryCheckin.get(position);
        if (model != null) {
            holder.txtPartnerName.setText(model.getPartnerName());
            String strPayment = Utils.formatCurrency("", model.getPayment());
            holder.txtDate.setText(Utils.getLanguageByResId(R.string.Payment_Date) + ": " + Utils.formatDate(Constant.DATE_SERVER_FORMAT, Constant.DATE_FORMAT_DMY, model.getDate()));
            ImageUtils.loadImageNoRadius(context, holder.imgPartner, model.getPicture());
            holder.txtDiscount.setText(Utils.getLanguageByResId(R.string.Discount) + ": " + model.getDiscount() + "%");
            holder.txtPayment.setText(Utils.getLanguageByResId(R.string.Payment) + ": " + strPayment + " VND");
            holder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onClick(v, position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return CollectionUtils.isEmpty(lstHistoryCheckin) ? 0 : lstHistoryCheckin.size();
    }

    public class CheckinViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.imgPartner)
        ImageView imgPartner;

        @BindView(R.id.txtPartnerName)
        TextView txtPartnerName;

        @BindView(R.id.txtPayment)
        TextView txtPayment;

        @BindView(R.id.txtDiscount)
        TextView txtDiscount;

        @BindView(R.id.txtDate)
        TextView txtDate;

        public View view;

        public CheckinViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            ButterKnife.bind(this, itemView);
        }
    }

    public void removeItemAtPosition(ArrayList<Integer> pos) {
        for (int j = pos.size() - 1; j >= 0; j--) {
            int position = pos.get(j);
            lstHistoryCheckin.remove(position);
            notifyDataSetChanged();
        }
    }

    public void removeAt(int position) {
        lstHistoryCheckin.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, lstHistoryCheckin.size());
    }
}
