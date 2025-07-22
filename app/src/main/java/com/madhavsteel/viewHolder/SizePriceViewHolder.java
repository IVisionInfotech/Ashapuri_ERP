package com.madhavsteel.viewHolder;

import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.madhavsteel.R;
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;

public class SizePriceViewHolder extends ChildViewHolder {

    private Context context;
    public TextView tvTitle, tvSize, tvPrice, tvTotalPrice, tvMultiply, tvSubTitle;
    public ImageView ivRupee;

    public SizePriceViewHolder(View view, Context context) {
        super(view);

        this.context = context;

        this.tvTitle = view.findViewById(R.id.tvTitle);
        this.tvSize = view.findViewById(R.id.tvSize);
        this.tvPrice = view.findViewById(R.id.tvPrice);
        this.tvTotalPrice = view.findViewById(R.id.tvTotalPrice);
        this.tvMultiply = view.findViewById(R.id.tvMultiply);
        this.tvSubTitle = view.findViewById(R.id.tvSubTitle);

        this.ivRupee = view.findViewById(R.id.ivRupee);
    }
}
