package com.madhavsteel.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.madhavsteel.R;
import com.madhavsteel.model.EditModel;
import com.madhavsteel.model.Sizes;
import com.madhavsteel.views.MySpinnerAdapter;

import java.util.ArrayList;
import java.util.List;

public class SelectedSizeListAdapter extends RecyclerView.Adapter implements Filterable {
    private AdapterListener listener, listener2;
    private Context context;
    private List<Sizes> list;
    private List<Sizes> listFilter;
    private List<String> measurementList = new ArrayList<>();
    public static ArrayList<EditModel> editModelArrayList = new ArrayList<>();

    public SelectedSizeListAdapter(Context context, List<Sizes> list, ArrayList<EditModel> editModelArrayList, AdapterListener listener, AdapterListener listener2) {
        this.context = context;
        this.list = list;
        this.listFilter = list;
        this.editModelArrayList = editModelArrayList;
        this.listener = listener;
        this.listener2 = listener2;
        measurementList.add("Tonne");
        measurementList.add("Kilogram");
    }

    @Override
    public CategoryView onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater mInflater = LayoutInflater.from(parent.getContext());
        ViewGroup mainGroup = (ViewGroup) mInflater.inflate(R.layout.recyclerview_selected_size_list_item, parent, false);
        final CategoryView listHolder = new CategoryView(mainGroup);
        mainGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        return listHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        final Sizes model = listFilter.get(position);
        final CategoryView mainHolder = (CategoryView) holder;

        mainHolder.tvTitle.setText(model.getSizeTitle());
        mainHolder.tvSize.setText(model.getWidth() + " x " + model.getHeight());

        mainHolder.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemSelected(model);
            }
        });

        editModelArrayList.get(position).setSizeId(model.getSizeId());

        mainHolder.etQuantity.setText(editModelArrayList.get(position).getEditTextValue());
        mainHolder.etQuantity.setSelection(mainHolder.etQuantity.getText().length());

        mainHolder.etPieceQuantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                editModelArrayList.get(position).setPieceQuantity(mainHolder.etPieceQuantity.getText().toString());
                if (charSequence.toString().length() > 0) {
                    int nosKg = Integer.parseInt(model.getNosKg());
                    int quantity = Integer.parseInt(mainHolder.etPieceQuantity.getText().toString());
                    int piece = quantity * nosKg;
                    mainHolder.etQuantity.setText(String.valueOf(piece));
                    mainHolder.spnMeasurement.setSelection(1);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        MySpinnerAdapter mySpinnerAdapter = new MySpinnerAdapter(context, android.R.layout.simple_spinner_item, measurementList);
        mySpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mainHolder.spnMeasurement.setAdapter(mySpinnerAdapter);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return (null != listFilter ? listFilter.size() : 0);
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    listFilter = list;
                } else {
                    List<Sizes> filteredList = new ArrayList<>();
                    for (Sizes row : list) {
                        if (row.getSizeTitle().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }
                    listFilter = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = listFilter;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                listFilter = (ArrayList<Sizes>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface AdapterListener {
        void onItemSelected(Sizes model);
    }

    public class CategoryView extends RecyclerView.ViewHolder {
        public TextView tvTitle, tvSize;
        public ImageView ivDelete;
        public Spinner spnMeasurement;
        public EditText etQuantity, etPieceQuantity;

        CategoryView(View view) {
            super(view);

            this.tvTitle = view.findViewById(R.id.tvTitle);
            this.tvSize = view.findViewById(R.id.tvSize);

            this.ivDelete = view.findViewById(R.id.ivDelete);

            this.spnMeasurement = view.findViewById(R.id.spnMeasurement);

            this.etQuantity = view.findViewById(R.id.etQuantity);
            this.etPieceQuantity = view.findViewById(R.id.etPrice);

            this.etQuantity.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    listener2.onItemSelected(listFilter.get(getAdapterPosition()));
                    editModelArrayList.get(getAdapterPosition()).setEditTextValue(etQuantity.getText().toString());
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

            this.spnMeasurement.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    listener2.onItemSelected(listFilter.get(getAdapterPosition()));
                    editModelArrayList.get(getAdapterPosition()).setSpinnerValue(spnMeasurement.getSelectedItem().toString());
                    etQuantity.requestFocus();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
    }

}