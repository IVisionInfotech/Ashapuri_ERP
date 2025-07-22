package com.madhavsteel.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.madhavsteel.R;
import com.madhavsteel.model.Product;
import com.madhavsteel.utils.Constant;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

/**
 * Created by wolfsoft3 on 16/7/18.
 */

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.MyViewHolder> {

    private static String TAG = "ProductListAdapter";
    private AdapterListener listener;
    private Context context;
    private List<Product> list;


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater mInflater = LayoutInflater.from(parent.getContext());
        final ViewGroup mainGroup = (ViewGroup) mInflater.inflate(R.layout.recyclerview_category_list_item, parent, false);
        final MyViewHolder listHolder = new MyViewHolder(mainGroup);

        mainGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemSelected(list.get(listHolder.getAdapterPosition()));
            }
        });
        return listHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        Product model = list.get(position);

        holder.tvTitle.setText(model.getProductTitle());

        String image;
        if (!model.getProductThumbImage().isEmpty() && !model.getProductThumbImage().equals("")) {
            image = Constant.imageBaseURL + model.getProductThumbImage();
        } else if (!model.getProductMediumImage().isEmpty() && !model.getProductMediumImage().equals("")) {
            image = Constant.imageBaseURL + model.getProductMediumImage();
        } else {
            image = Constant.imageBaseURL + model.getProductImage();
        }

        Glide.with(context)
                .load(image)
                    .into(holder.ivImage);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        RoundedImageView ivImage;
        TextView tvTitle;

        public MyViewHolder(View itemView) {
            super(itemView);

            ivImage = itemView.findViewById(R.id.ivImage);

            tvTitle = itemView.findViewById(R.id.tvTitle);
        }
    }

    public ProductListAdapter(Context mainActivityContacts, List<Product> offerList, AdapterListener listener) {
        this.list = offerList;
        this.context = mainActivityContacts;
        this.listener = listener;
    }

    public interface AdapterListener {
        void onItemSelected(Product cityModel);
    }
}
