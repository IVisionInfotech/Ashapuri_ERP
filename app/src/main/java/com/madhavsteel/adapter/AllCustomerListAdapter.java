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
import com.madhavsteel.utils.ClickListener;
import com.madhavsteel.utils.Session;

import java.util.ArrayList;
import java.util.List;

public class AllCustomerListAdapter extends RecyclerView.Adapter implements Filterable {

    private static final String TAG = "CustomerListAdapter";
    private ClickListener listener, listener2, listener3;
    private Context context;
    private Session session;
    private List<Customer> list;
    private List<Customer> filterList;

    public AllCustomerListAdapter(Context context, List<Customer> list, ClickListener listener, ClickListener listener2, ClickListener listener3) {
        this.context = context;
        session = new Session(context);
        this.list = list;
        this.filterList = list;
        this.listener = listener;
        this.listener2 = listener2;
        this.listener3 = listener3;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater mInflater = LayoutInflater.from(parent.getContext());
        ViewGroup mainGroup = (ViewGroup) mInflater.inflate(R.layout.recyclerview_all_customer_list_item, parent, false);
        final MyViewHolder listHolder = new MyViewHolder(mainGroup);
        mainGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemSelected(listHolder.getAdapterPosition());
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
        mainHolder.tvAddress.setText(model.getAddress());
//        mainHolder.tvDateTimeAdded.setText(model.getDateTimeAdded());

        mainHolder.llTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notifyDataSetChanged();
                listener.onItemSelected(position);
            }
        });

        mainHolder.ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener2.onItemSelected(position);
            }
        });
        mainHolder.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener3.onItemSelected(position);
            }
        });
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
                        if (row.getName().toLowerCase().contains(charString.toLowerCase())) {
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
        public TextView tvTitle, tvContact, tvAddress;
        public ImageView ivImage, ivEdit, ivDelete;
        public LinearLayout llTitle;

        MyViewHolder(View view) {
            super(view);

            tvTitle = view.findViewById(R.id.tvTitle);
            tvContact = view.findViewById(R.id.tvContact);
            tvAddress = view.findViewById(R.id.tvAddress);
            llTitle = view.findViewById(R.id.llTitle);
            ivImage = view.findViewById(R.id.ivImage);
            ivEdit = view.findViewById(R.id.ivEdit);
            ivDelete = view.findViewById(R.id.ivDelete);
        }
    }
}
