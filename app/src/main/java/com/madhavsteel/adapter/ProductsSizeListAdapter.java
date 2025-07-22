package com.madhavsteel.adapter;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.madhavsteel.R;
import com.madhavsteel.model.EditModel;
import com.madhavsteel.model.Products;
import com.madhavsteel.model.Sizes;
import com.madhavsteel.viewHolder.ProductViewHolder;
import com.madhavsteel.viewHolder.SizeViewHolder;
import com.madhavsteel.views.MySpinnerAdapter;
import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.ArrayList;
import java.util.List;

public class ProductsSizeListAdapter extends ExpandableRecyclerViewAdapter<ProductViewHolder, SizeViewHolder> {

    private String TAG = "ProductsSizeListAdapter";
    private AdapterListener listener, listener2;
    private AdapterListenerProducts listenerProducts;
    private Context context;
    public static ArrayList<EditModel> editModelArrayList = new ArrayList<>();

    public interface AdapterListener {
        void onItemSelected(Sizes model);
    }

    public interface AdapterListenerProducts {
        void onItemSelected(Products model);
    }

    public ProductsSizeListAdapter(Context context, List<? extends ExpandableGroup> groups, ArrayList<EditModel> editModelArrayList, AdapterListener listener, AdapterListener listener2, AdapterListenerProducts listenerProducts) {
        super(groups);
        this.context = context;
        this.listener = listener;
        this.listener2 = listener2;
        this.listenerProducts = listenerProducts;
        editModelArrayList.clear();
    }

    @Override
    public ProductViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.expandable_recyclerview_product_list_item, parent, false);

        return new ProductViewHolder(view);
    }

    @Override
    public SizeViewHolder onCreateChildViewHolder(ViewGroup parent, final int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.recyclerview_selected_size_list_item, parent, false);

        return new SizeViewHolder(view, context);
    }

    @Override
    public void onBindChildViewHolder(SizeViewHolder holder, final int flatPosition, ExpandableGroup group, int childIndex) {
        final Sizes model = ((Products) group).getItems().get(childIndex);
        final SizeViewHolder mainHolder = (SizeViewHolder) holder;

        mainHolder.tvTitle.setText(model.getSizeTitle());
        if (!model.getWidth().equals("") && !model.getWidth().equals("null") && !model.getWidth().isEmpty() && !model.getHeight().equals("") && !model.getHeight().equals("null") && !model.getHeight().isEmpty()) {
            mainHolder.tvSize.setText(model.getWidth() + " x " + model.getHeight());
        } else {
            mainHolder.tvSize.setVisibility(View.GONE);
        }

        mainHolder.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemSelected(model);
            }
        });

        editModelArrayList.get(Integer.parseInt(model.getPosition())).setSizeId(model.getSizeId());

        mainHolder.etPieceQuantity.setText(model.getPieceQuantity());
        mainHolder.etPieceQuantity.setSelection(mainHolder.etPieceQuantity.getText().length());
        mainHolder.etQuantity.setText(model.getQuantity());
        mainHolder.etQuantity.setSelection(mainHolder.etQuantity.getText().length());
        mainHolder.etPrice.setText(model.getPrice());
        mainHolder.etPrice.setSelection(mainHolder.etPrice.getText().length());

        List<String> measurementList = new ArrayList<>();

        if (model.getShowPieceStatus().equals("1")) {
            measurementList.add("Piece");
        } else {
//            measurementList.add("Tonne");
            measurementList.add("Kilogram");
        }

        MySpinnerAdapter mySpinnerAdapter = new MySpinnerAdapter(context, android.R.layout.simple_spinner_item, measurementList);
        mySpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mainHolder.spnMeasurement.setAdapter(mySpinnerAdapter);

        mainHolder.etPieceQuantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                listener2.onItemSelected(model);
                editModelArrayList.get(Integer.parseInt(model.getPosition())).setPieceInQuantity(mainHolder.etPieceQuantity.getText().toString());
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
                listener2.onItemSelected(model);
                editModelArrayList.get(Integer.parseInt(model.getPosition())).setEditTextValue(mainHolder.etQuantity.getText().toString());
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
                editModelArrayList.get(Integer.parseInt(model.getPosition())).setRate(mainHolder.etPrice.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        for (int i = 0; i < measurementList.size(); i++) {
            if (model.getMeasurement().toLowerCase().equals(measurementList.get(i).toLowerCase())) {
                mainHolder.spnMeasurement.setSelection(i);
            }
        }

        mainHolder.spnMeasurement.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                listener2.onItemSelected(model);
                editModelArrayList.get(Integer.parseInt(model.getPosition())).setSpinnerValue(mainHolder.spnMeasurement.getSelectedItem().toString());
                mainHolder.etPieceQuantity.requestFocus();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onBindGroupViewHolder(ProductViewHolder holder, int flatPosition, ExpandableGroup group) {
        final Products product = ((Products) group);
        holder.setGroupName(group, product);
        holder.expand();

        final ProductViewHolder mainHolder = (ProductViewHolder) holder;
        mainHolder.llPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listenerProducts.onItemSelected(product);
            }
        });

        for (int i = 0; i < product.getSizeList().size(); i++) {
            Sizes model = product.getSizeList().get(i);
            EditModel editModel = new EditModel();
            editModel.setCartId(model.getCartId());
            editModel.setSizeId(model.getSizeId());
            editModel.setEditTextValue(model.getQuantity());
            editModel.setPieceInQuantity(model.getPieceQuantity());
            editModel.setRate(model.getPrice());
            editModel.setSpinnerValue(model.getMeasurement());
            editModelArrayList.add(editModel);
        }
    }
}