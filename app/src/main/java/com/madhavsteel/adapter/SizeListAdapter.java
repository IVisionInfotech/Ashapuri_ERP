package com.madhavsteel.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.madhavsteel.R;
import com.madhavsteel.activity.PreLoginActivity;
import com.madhavsteel.model.EditModel;
import com.madhavsteel.model.Sizes;
import com.madhavsteel.utils.ClickListener;
import com.madhavsteel.utils.Constant;
import com.madhavsteel.utils.Session;
import com.madhavsteel.views.MySpinnerAdapter;

import java.util.ArrayList;
import java.util.List;

public class SizeListAdapter extends RecyclerView.Adapter implements Filterable {

    private static String TAG = "SizeListAdapter";
    private ClickListener listener, listener2;
    private Context context;
    private Session session;
    private List<Sizes> list;
    private List<Sizes> listFilter;
    private String userId;
    public static ArrayList<EditModel> editModelArrayList = new ArrayList<>();
    private ProgressDialog pDialog;

    public SizeListAdapter(Context context, List<Sizes> list, ClickListener listener, ClickListener listener2) {
        this.context = context;
        this.list = list;
        this.listFilter = list;
        this.listener = listener;
        this.listener2 = listener2;
        this.session = new Session(context);
    }

    @Override
    public CategoryView onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater mInflater = LayoutInflater.from(parent.getContext());
        ViewGroup mainGroup = (ViewGroup) mInflater.inflate(R.layout.recyclerview_size_list_item, parent, false);
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

        mainHolder.cbTitle.setText(model.getSizeTitle());

        List<String> measurementList = new ArrayList<>();

        if (model.getShowPieceStatus().equals("1")) {
            measurementList.add("Piece");
        } else {
            measurementList.add("Kilogram");
//            measurementList.add("Tonne");
        }

        MySpinnerAdapter mySpinnerAdapter = new MySpinnerAdapter(context, android.R.layout.simple_spinner_item, measurementList);
        mySpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mainHolder.spnMeasurement.setAdapter(mySpinnerAdapter);

        if (model.getCartStatus().equals("1")) {
            mainHolder.cbTitle.setChecked(true);
            mainHolder.cbTitle.setTextColor(context.getResources().getColor(R.color.colorAccent));
            mainHolder.etPieceQuantity.setText(model.getPieceQuantity());
            mainHolder.etQuantity.setText(model.getQuantity());
            mainHolder.etPrice.setText(model.getPrice());
            mainHolder.etQuantity.setSelection(mainHolder.etQuantity.getText().toString().length());
            mainHolder.etPrice.setSelection(mainHolder.etPrice.getText().toString().length());
            for (int i = 0; i < measurementList.size(); i++) {
                if (model.getMeasurement().toLowerCase().equals(measurementList.get(i).toLowerCase())) {
                    mainHolder.spnMeasurement.setSelection(i);
                }
            }
            EditModel editModel = new EditModel();
            editModel.setSizeId(model.getSizeId());
            editModel.setPieceInQuantity(model.getPieceQuantity());
            editModel.setEditTextValue(model.getQuantity());
            editModel.setRate(model.getPrice());
            editModel.setSpinnerValue(model.getMeasurement());
            editModelArrayList.add(editModel);
            Constant.selectedSizeList.add(model.getSizeId());
            Constant.sizeList.add(model);
            mainHolder.llMeasurement.setVisibility(View.VISIBLE);
            mainHolder.llPieceQuantity.setVisibility(View.VISIBLE);
            mainHolder.llQuantity.setVisibility(View.VISIBLE);
            mainHolder.llPrice.setVisibility(View.VISIBLE);
//            mainHolder.llDetails.setVisibility(View.VISIBLE);

        } else {
            mainHolder.etPieceQuantity.setText("0");
            mainHolder.etQuantity.setText("1");
            mainHolder.etPrice.setText("0");
            mainHolder.cbTitle.setChecked(false);
            mainHolder.llMeasurement.setVisibility(View.GONE);
            mainHolder.llPieceQuantity.setVisibility(View.GONE);
            mainHolder.llQuantity.setVisibility(View.GONE);
            mainHolder.llPrice.setVisibility(View.GONE);
//            mainHolder.llDetails.setVisibility(View.GONE);
        }

        mainHolder.cbTitle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (session.getLoginStatus()) {
                        mainHolder.llMeasurement.setVisibility(View.VISIBLE);
                        mainHolder.llPieceQuantity.setVisibility(View.VISIBLE);
                        mainHolder.llQuantity.setVisibility(View.VISIBLE);
                        mainHolder.llPrice.setVisibility(View.VISIBLE);
//                        mainHolder.llDetails.setVisibility(View.VISIBLE);
                        if (!Constant.selectedSizeList.contains(model.getSizeId())) {

                            EditModel editModel = new EditModel();
                            editModel.setSizeId(model.getSizeId());
                            editModel.setPieceInQuantity("0");
                            editModel.setEditTextValue("1");
                            editModel.setRate("0");
                            editModel.setSpinnerValue(mainHolder.spnMeasurement.getSelectedItem().toString());
                            editModelArrayList.add(editModel);

                            Constant.selectedSizeList.add(model.getSizeId());
                            Constant.sizeList.add(model);

                            mainHolder.cbTitle.setTextColor(context.getResources().getColor(R.color.colorAccent));
                        }
                    } else {
                        goToActivity(context, PreLoginActivity.class);
                    }
                } else {
                    mainHolder.llMeasurement.setVisibility(View.GONE);
                    mainHolder.llPieceQuantity.setVisibility(View.GONE);
                    mainHolder.llQuantity.setVisibility(View.GONE);
                    mainHolder.llPrice.setVisibility(View.GONE);
//                    mainHolder.llDetails.setVisibility(View.GONE);
                    if (Constant.selectedSizeList.contains(model.getSizeId())) {
                        Constant.selectedSizeList.remove(model.getSizeId());
                        Log.e(TAG, "onCheckedChanged: 1");
                        for (int i = 0; i < Constant.sizeList.size(); i++) {
                            if (model.getSizeId().equals(Constant.sizeList.get(i).getSizeId())) {
                                Constant.sizeList.remove(i);
                                Log.e(TAG, "onCheckedChanged: 2");
                            }
                            Log.e(TAG, "onCheckedChanged: test " + editModelArrayList.get(i).getSizeId() + " : " + model.getSizeId());
                            if (editModelArrayList.get(i).getSizeId().equals(model.getSizeId())) {
                                Log.e(TAG, "onCheckedChanged: 3");
                                editModelArrayList.remove(i);
                            }
                        }
                    }
                    listener2.onItemSelected(position);
                    mainHolder.cbTitle.setTextColor(context.getResources().getColor(R.color.black));
                }
                Log.e("OnCheckedChange", Constant.selectedSizeList + "");
                Log.e("OnCheckedChange", editModelArrayList.size() + "");
            }
        });

        mainHolder.etPieceQuantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                for (int h = 0; h < editModelArrayList.size(); h++) {
                    if (editModelArrayList.get(h).getSizeId().equals(model.getSizeId())) {
                        editModelArrayList.get(h).setPieceInQuantity(mainHolder.etPieceQuantity.getText().toString());
                        break;
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mainHolder.etQuantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                for (int h = 0; h < editModelArrayList.size(); h++) {
                    if (editModelArrayList.get(h).getSizeId().equals(model.getSizeId())) {
                        editModelArrayList.get(h).setEditTextValue(mainHolder.etQuantity.getText().toString());
                        break;
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mainHolder.etPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().length() > 0) {
                    for (int h = 0; h < editModelArrayList.size(); h++) {
                        if (editModelArrayList.get(h).getSizeId().equals(model.getSizeId())) {
                            editModelArrayList.get(h).setRate(mainHolder.etPrice.getText().toString());
                            break;
                        }
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mainHolder.spnMeasurement.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                for (int h = 0; h < editModelArrayList.size(); h++) {
                    if (editModelArrayList.get(h).getSizeId().equals(model.getSizeId())) {
                        editModelArrayList.get(h).setSpinnerValue(mainHolder.spnMeasurement.getSelectedItem().toString());
                    }
                }

                mainHolder.etQuantity.requestFocus();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mainHolder.etQuantity.setSelection(mainHolder.etQuantity.getText().toString().length());
    }

    public void goToActivity(Context context, Class aClass) {
        Intent intent = new Intent(context, aClass);
        context.startActivity(intent);
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
        void onItemSelected();
    }

    public class CategoryView extends RecyclerView.ViewHolder {

        public CheckBox cbTitle;
        public TextView tvSubTitle;
        public Spinner spnMeasurement;
        public EditText etPieceQuantity, etQuantity, etPrice;
        public LinearLayout llMeasurement, llPieceQuantity, llQuantity, llPrice;

        CategoryView(View view) {
            super(view);

            this.cbTitle = view.findViewById(R.id.cbTitle);

            this.tvSubTitle = view.findViewById(R.id.tvSubTitle);

            this.spnMeasurement = view.findViewById(R.id.spnMeasurement);

            this.etPieceQuantity = view.findViewById(R.id.etPieceQuantity);
            this.etQuantity = view.findViewById(R.id.etQuantity);
            this.etPrice = view.findViewById(R.id.etPrice);

            this.llMeasurement = view.findViewById(R.id.llMeasurement);
            this.llPieceQuantity = view.findViewById(R.id.llPieceQuantity);
            this.llQuantity = view.findViewById(R.id.llQuantity);
            this.llPrice = view.findViewById(R.id.llPrice);
        }
    }

    public void showProgressDialog(Context context, String msg) {
        pDialog = new ProgressDialog(context);
        pDialog.setCancelable(false);
        pDialog.setMessage(msg);
        if (!((Activity) context).isFinishing()) {
            pDialog.show();
        }
    }

    public void hideProgressDialog() {
        if (pDialog != null) {
            if (pDialog.isShowing()) {
                pDialog.dismiss();
            }
        }
    }
}