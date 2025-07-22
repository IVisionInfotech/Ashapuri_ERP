package com.madhavsteel.viewHolder;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.madhavsteel.R;
import com.madhavsteel.model.Order;
import com.madhavsteel.model.Products;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder;

public class ProductViewHolder extends GroupViewHolder {

    private String TAG = "ProductViewHolder";
    private TextView tvTitle, tvSubTitle;
    private ImageView ivArrow, ivEdit;
    public LinearLayout llPrice;

    public ProductViewHolder(View itemView) {
        super(itemView);

        tvTitle = itemView.findViewById(R.id.tvTitle);
        tvSubTitle = itemView.findViewById(R.id.tvSubTitle);

        ivArrow = itemView.findViewById(R.id.ivArrow);
        ivEdit = itemView.findViewById(R.id.ivEdit);

        llPrice = itemView.findViewById(R.id.llPrice);
    }

    @Override
    public void expand() {
        ivArrow.setImageResource(R.drawable.ic_arrow_down);
        Log.e("Adapter", "expand");
    }

    @Override
    public void collapse() {
        ivArrow.setImageResource(R.drawable.ic_arrow_up);
        Log.e("Adapter", "collapse");
    }

    public void setGroupName(ExpandableGroup group, Products model) {
        tvTitle.setText(group.getTitle());
//        tvSubTitle.setText(String.format("%,d", Integer.parseInt(String.format("%.0f", Float.parseFloat(model.getProductPrice())))));
        llPrice.setVisibility(View.GONE);
        ivEdit.setVisibility(View.GONE);
        group.describeContents();
        expand();
    }

    public void setGroupName(ExpandableGroup group, Order model) {
        tvTitle.setText(group.getTitle());
        tvSubTitle.setText(String.format("%.2f", Float.parseFloat(String.format("%.2f", Float.parseFloat(model.getGrandTotal())))));
        llPrice.setVisibility(View.VISIBLE);
        expand();
    }

    public void setGroupName(ExpandableGroup group, String total) {
        tvTitle.setText(group.getTitle());
        tvSubTitle.setText(String.format("%.2f", Float.parseFloat(String.format("%.2f", Float.parseFloat(total)))));
        llPrice.setVisibility(View.VISIBLE);
        expand();
    }
}
