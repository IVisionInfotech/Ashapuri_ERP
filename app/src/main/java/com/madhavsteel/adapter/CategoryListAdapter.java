package com.madhavsteel.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.madhavsteel.R;
import com.madhavsteel.model.Category;
import com.madhavsteel.utils.Constant;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

/**
 * Created by wolfsoft3 on 16/7/18.
 */

public class CategoryListAdapter extends RecyclerView.Adapter<CategoryListAdapter.MyViewHolder> {

    private static String TAG = "CategoryListAdapter";
    private AdapterListener listener;
    private Context context;
    private List<Category> list;


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
        Category model = list.get(position);

        holder.tvTitle.setText(model.getCatTitle());

        String image;
        if (!model.getCatThumbImage().isEmpty() && !model.getCatThumbImage().equals("")) {
            image = Constant.imageBaseURL + model.getCatThumbImage();
        } else if (!model.getCatMediumImage().isEmpty() && !model.getCatMediumImage().equals("")) {
            image = Constant.imageBaseURL + model.getCatMediumImage();
        } else {
            image = Constant.imageBaseURL + model.getCatImage();
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

    public CategoryListAdapter(Context mainActivityContacts, List<Category> offerList, AdapterListener listener) {
        this.list = offerList;
        this.context = mainActivityContacts;
        this.listener = listener;
    }

    public interface AdapterListener {
        void onItemSelected(Category cityModel);
    }
}
