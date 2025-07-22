package com.madhavsteel.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.madhavsteel.R;
import com.madhavsteel.model.Order;
import com.madhavsteel.model.OrderInquiry;
import com.madhavsteel.viewHolder.ProductViewHolder;
import com.madhavsteel.viewHolder.SizePriceViewHolder;
import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

public class PriceListAdapter extends ExpandableRecyclerViewAdapter<ProductViewHolder, SizePriceViewHolder> {

    private String TAG = "PriceListAdapter";
    private Context context;

    public interface AdapterListener {
        void onItemSelected(OrderInquiry model);
    }

    public PriceListAdapter(Context context, List<? extends ExpandableGroup> groups) {
        super(groups);
        this.context = context;
    }

    @Override
    public ProductViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.expandable_recyclerview_product_list_item, parent, false);

        return new ProductViewHolder(view);
    }

    @Override
    public SizePriceViewHolder onCreateChildViewHolder(ViewGroup parent, final int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.recyclerview_price_list_item, parent, false);

        return new SizePriceViewHolder(view, context);
    }

    @Override
    public void onBindChildViewHolder(SizePriceViewHolder holder, final int flatPosition, ExpandableGroup group, int childIndex) {
        final OrderInquiry model = ((Order) group).getItems().get(childIndex);
        final SizePriceViewHolder mainHolder = (SizePriceViewHolder) holder;

        mainHolder.tvTitle.setText(model.getSizeTitle());
        mainHolder.tvSize.setText(model.getQuantity() + " (" + model.getMeasurement() + ")");

        /*String price;
        float finalPrice;
        float tonnePrice = Float.parseFloat(model.getPrice());
        float kgPrice = tonnePrice / 1000;
        if (model.getMeasurement().toLowerCase().equals("tonne") || model.getMeasurement().toLowerCase().equals("piece")) {
            price = String.format("%,d", Integer.parseInt(String.format("%.0f", tonnePrice))) + " + " + String.format("%.2f", Float.parseFloat(model.getDifferentPrice().equals("null") ? "0" : model.getDifferentPrice()));
            finalPrice = tonnePrice + Float.parseFloat(model.getDifferentPrice().equals("null") ? "0" : model.getDifferentPrice());
        } else {
            price = String.format("%,d", Integer.parseInt(String.format("%.0f", kgPrice))) + " + " + String.format("%.2f", Float.parseFloat(model.getDifferentKgPrice().equals("null") ? "0" : model.getDifferentKgPrice()));
            finalPrice = kgPrice + Float.parseFloat(model.getDifferentKgPrice().equals("null") ? "0" : model.getDifferentKgPrice());
        }*/

        mainHolder.tvSubTitle.setText(model.getPieceQuantity() + " Piece");
        mainHolder.tvSubTitle.setVisibility(View.VISIBLE);
        mainHolder.tvPrice.setText(String.format("%.2f", Float.parseFloat(model.getPrice())));
        float totalPrice = Float.parseFloat(model.getQuantity()) * Float.parseFloat(model.getPrice());
        mainHolder.tvTotalPrice.setText(String.format("%.2f", Float.parseFloat(String.format("%.2f", totalPrice))));
    }

    @Override
    public void onBindGroupViewHolder(ProductViewHolder holder, int flatPosition, ExpandableGroup group) {
        final Order model = ((Order) group);
        holder.setGroupName(group, model);
    }
}