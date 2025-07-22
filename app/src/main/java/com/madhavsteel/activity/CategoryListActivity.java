package com.madhavsteel.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.madhavsteel.R;
import com.madhavsteel.adapter.CategoryListAdapter;
import com.madhavsteel.model.Category;
import com.madhavsteel.model.Product;

import java.util.ArrayList;

public class CategoryListActivity extends BaseActivity {

    private static final String TAG = "CategoryListActivity";
    private Context context;
    private ImageView ivBack;
    private TextView tvTitle;
    private RecyclerView recyclerView;
    private CategoryListAdapter adapter;
    private LinearLayout llList, llNoList;
    private ArrayList<Category> list = new ArrayList<>();
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_list);

        context = CategoryListActivity.this;

        init();

        if (getIntent() != null) {
            if (getIntent().hasExtra("details")) {
                Product model = (Product) getIntent().getSerializableExtra("details");
                bindData(model);
            }
        }
    }

    private void init() {
        tvTitle = findViewById(R.id.tvTitle);

        ivBack = findViewById(R.id.ivBack);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        llList = findViewById(R.id.llList);
        llNoList = findViewById(R.id.llNoList);

        recyclerView = findViewById(R.id.recyclerView);

        mLayoutManager = new GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        recyclerView.setItemViewCacheSize(20);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private void bindData(Product model) {

        tvTitle.setText(model.getProductTitle());

        list.addAll(model.getCategoryArrayList());

        adapter = new CategoryListAdapter(context, list, new CategoryListAdapter.AdapterListener() {
            @Override
            public void onItemSelected(Category model) {
                Intent intent;
                if (model.getAddStatus().equals("1")) {
                    intent = new Intent(context, AddSizeListActivity.class);
                } else {
                    intent = new Intent(context, SizeListActivity.class);
                }
                intent.putExtra("details", model);
                startActivityForResult(intent, 12);
            }
        });

        if (list != null) {
            if (list.size() > 0) {
                llList.setVisibility(View.VISIBLE);
                llNoList.setVisibility(View.GONE);
            } else {
                llList.setVisibility(View.GONE);
                llNoList.setVisibility(View.VISIBLE);
            }
        } else {
            llList.setVisibility(View.GONE);
            llNoList.setVisibility(View.VISIBLE);
        }

        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 12) {
            if (resultCode == Activity.RESULT_OK) {
                setResultOfActivity(11);
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                setCancelResultOfActivity(11);
            }
        }
    }
}
