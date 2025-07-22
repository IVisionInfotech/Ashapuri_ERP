package com.madhavsteel.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.madhavsteel.R;
import com.madhavsteel.model.Notification;
import com.madhavsteel.utils.Constant;

import java.util.List;

/**
 * Created by wolfsoft3 on 16/7/18.
 */

public class NotificationsListAdapter extends RecyclerView.Adapter<NotificationsListAdapter.MyViewHolder> {

    private static final String TAG = "NotificationsListAdapter";
    private Context context;
    private List<Notification> list;
    private AdapterListener listener;


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater mInflater = LayoutInflater.from(parent.getContext());
        ViewGroup mainGroup = (ViewGroup) mInflater.inflate(R.layout.recyclerview_notifications_list_item, parent, false);
        final MyViewHolder listHolder = new MyViewHolder(mainGroup);
        mainGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        return listHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final Notification model = list.get(position);

        holder.tvTitle.setText(model.getTitle());
        holder.tvBody.setText(model.getBody());
        holder.tvDate.setText(model.getDateTimeAdded());

        if (model.getNotificationType().equals("1")) {
            Glide.with(context)
                    .load(Constant.imageBaseURL + model.getImage())
                    .into(holder.ivImage);
            holder.ivImage.setVisibility(View.VISIBLE);
        } else {
            holder.ivImage.setVisibility(View.GONE);
        }

        holder.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemSelected(list.get(position));
            }
        });
        holder.ivCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number = context.getResources().getString(R.string.contact);
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:" + number));
                context.startActivity(callIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView ivImage, ivCall, ivDelete;
        TextView tvTitle, tvBody, tvDate;


        public MyViewHolder(View itemView) {
            super(itemView);

            ivImage = itemView.findViewById(R.id.ivImage);
            ivCall = itemView.findViewById(R.id.ivCall);
            ivDelete = itemView.findViewById(R.id.ivDelete);

            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvBody = itemView.findViewById(R.id.tvBody);
            tvDate = itemView.findViewById(R.id.tvDate);
        }
    }

    public NotificationsListAdapter(Context mainActivityContacts, List<Notification> offerList, AdapterListener listener) {
        this.list = offerList;
        this.context = mainActivityContacts;
        this.listener = listener;
    }

    public interface AdapterListener {
        void onItemSelected(Notification model);
    }
}
