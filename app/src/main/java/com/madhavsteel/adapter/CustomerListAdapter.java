package com.madhavsteel.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.madhavsteel.R;
import com.madhavsteel.model.Customer;
import com.madhavsteel.utils.Constant;
import com.madhavsteel.utils.Session;

import java.util.ArrayList;
import java.util.List;

public class CustomerListAdapter extends RecyclerView.Adapter implements Filterable {

    private static final String TAG = "CustomerListAdapter";
    private ClickListener listener;
    private Context context;
    private Session session;
    private List<Customer> list;
    private List<Customer> filterList;

    public CustomerListAdapter(Context context, List<Customer> list, ClickListener listener) {
        this.context = context;
        session = new Session(context);
        this.list = list;
        this.filterList = list;
        this.listener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater mInflater = LayoutInflater.from(parent.getContext());
        ViewGroup mainGroup = (ViewGroup) mInflater.inflate(R.layout.recyclerview_customer_list_item, parent, false);
        final MyViewHolder listHolder = new MyViewHolder(mainGroup);
        mainGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemSelected(filterList.get(listHolder.getAdapterPosition()));
            }
        });
        return listHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final Customer model = filterList.get(position);
        final MyViewHolder mainHolder = (MyViewHolder) holder;

        mainHolder.tvTitle.setText(model.getName());
        mainHolder.tvContact.setText(model.getContact());

        if (model.getId().equals(Constant.customerId)) {
            mainHolder.llTitle.setBackground(context.getResources().getDrawable(R.drawable.selected_border));
            mainHolder.tvTitle.setTextColor(context.getResources().getColor(R.color.lightBlack));
        } else {
            mainHolder.llTitle.setBackground(context.getResources().getDrawable(R.drawable.unselected_border));
            mainHolder.tvTitle.setTextColor(context.getResources().getColor(R.color.darkgray));
        }

        mainHolder.llTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notifyDataSetChanged();
                listener.onItemSelected(model);
            }
        });

        mainHolder.ivImage.setImageResource(R.drawable.user);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return (null != filterList ? filterList.size() : 0);
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    filterList = list;
                } else {
                    List<Customer> newFilteredList = new ArrayList<>();
                    for (Customer row : list) {
                        if (row.getName().toLowerCase().contains(charString.toLowerCase()) || row.getContact().toLowerCase().contains(charString.toLowerCase())) {
                            newFilteredList.add(row);
                        }
                    }
                    filterList = newFilteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filterList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filterList = (ArrayList<Customer>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvTitle, tvContact;
        public ImageView ivImage;
        public LinearLayout llTitle;

        MyViewHolder(View view) {
            super(view);

            tvTitle = view.findViewById(R.id.tvTitle);
            tvContact = view.findViewById(R.id.tvContact);
            llTitle = view.findViewById(R.id.llTitle);
            ivImage = view.findViewById(R.id.ivImage);
        }
    }

    public interface ClickListener {

        void onItemSelected(Customer model);
    }
}
