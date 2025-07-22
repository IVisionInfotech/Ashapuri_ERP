package com.madhavsteel.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.madhavsteel.R;
import com.madhavsteel.adapter.PriceListAdapter;
import com.madhavsteel.model.Order;
import com.madhavsteel.model.OrderInquiry;
import com.madhavsteel.model.User;
import com.madhavsteel.utils.Constant;
import com.madhavsteel.utils.RealmController;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class PriceListActivity extends BaseActivity {

    private static final String TAG = "PriceListActivity";
    private Context context;
    private ImageView ivBack;
    private TextView tvTitle, tvCalculatePrice, tvCalculatedPrice, tvGSTPrice, tvTotal, tvOrder, tvInfo, tvProduct, tvProductPrice;
    private LinearLayout llCalculatedPrice;
    private RecyclerView recyclerView;
    private PriceListAdapter adapter;
    private LinearLayout llList, llNoList;
    private ArrayList<Order> list = new ArrayList<>();
    private RecyclerView.LayoutManager mLayoutManager;
    private String userId = "", userName = "", userContact = "";
    private int position = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recyclerview_list);

        context = PriceListActivity.this;
        RealmController.with(context).refresh();
        User user = RealmController.with(context).getUser();
        if (user != null) {
            userId = user.getUserId();
            userName = user.getName();
            userContact = user.getContact();
        }

        init();

        bindData();
    }

    private void init() {
        tvTitle = findViewById(R.id.tvTitle);
        tvProduct = findViewById(R.id.tvProduct);
        tvProductPrice = findViewById(R.id.tvProductPrice);
        tvCalculatePrice = findViewById(R.id.tvCalculatePrice);
        tvCalculatedPrice = findViewById(R.id.tvCalculatedPrice);
        tvGSTPrice = findViewById(R.id.tvGSTPrice);
        tvTotal = findViewById(R.id.tvTotal);
        tvOrder = findViewById(R.id.tvOrder);
        tvInfo = findViewById(R.id.tvInfo);
        tvInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number = getResources().getString(R.string.contact);
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:" + number));
                startActivity(callIntent);
            }
        });

        tvCalculatePrice.setVisibility(View.GONE);
        tvCalculatePrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (session.getLoginStatus()) {

                } else {
                    goToActivityForResult(context, PreLoginActivity.class, 24);
                }
            }
        });
        tvOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharePDF();
//                showDialog();
            }
        });

        ivBack = findViewById(R.id.ivBack);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        llCalculatedPrice = findViewById(R.id.llCalculatedPrice);
        llList = findViewById(R.id.llList);
        llNoList = findViewById(R.id.llNoList);

        recyclerView = findViewById(R.id.recyclerView);

        mLayoutManager = new GridLayoutManager(context, 1, GridLayoutManager.VERTICAL, false);
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

    @Override
    protected void onResume() {
        super.onResume();
        RealmController.with(context).refresh();
        User user = RealmController.with(context).getUser();
        if (user != null) {
            userId = user.getUserId();
            userName = user.getName();
            userContact = user.getContact();
        }
    }

    private void sharePDF() {

        String shareMessage = "\nLet me recommend you this quotation\n\n";
        shareMessage = shareMessage + Constant.pdfLink + "\n\n";

        try {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
            sendIntent.putExtra("jid", Constant.customerContact + "@s.whatsapp.net");
            sendIntent.setType("text/plain");
            sendIntent.setPackage("com.whatsapp");
            startActivity(sendIntent);
        } catch (Exception e) {
            e.printStackTrace();

            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
            sendIntent.putExtra("jid", Constant.customerContact + "@s.whatsapp.net");
            sendIntent.setType("text/plain");
            sendIntent.setPackage("com.whatsapp.w4b");
            startActivity(sendIntent);
        }
    }

    private void bindData() {

        tvTitle.setText("Calculated Price");
        String title = "", price = "";

        if (Constant.priceResult != null) {
            try {
                Constant.pdfLink = Constant.priceResult.optString("pdfLink");
                Constant.customerContact = Constant.priceResult.optString("customerContact");
                JSONArray priceArray = Constant.priceResult.optJSONArray("priceArray");
                for (int i = 0; i < priceArray.length(); i++) {
                    JSONObject object = priceArray.getJSONObject(i);

                    JSONArray orderInquiryArray = object.optJSONArray("orderInquiryArray");
                    ArrayList<OrderInquiry> orderInquiries = new ArrayList<>();
                    for (int j = 0; j < orderInquiryArray.length(); j++) {
                        JSONObject jsonObject = orderInquiryArray.getJSONObject(j);
                        OrderInquiry orderInquiry = new OrderInquiry();
                        orderInquiry.setQuoteDetailsId(jsonObject.optString("quoteDetailsId"));
                        orderInquiry.setQuoteId(jsonObject.optString("quoteId"));
                        orderInquiry.setCustomerId(jsonObject.optString("customerId"));
                        orderInquiry.setSizeId(jsonObject.optString("sizeId"));
                        orderInquiry.setSizeTitle(jsonObject.optString("sizeTitle"));
                        orderInquiry.setCatId(jsonObject.optString("catId"));
                        orderInquiry.setCatTitle(jsonObject.optString("catTitle"));
                        orderInquiry.setQuantity(jsonObject.optString("quantity"));
                        orderInquiry.setMeasurement(jsonObject.optString("measurement"));
                        orderInquiry.setProductId(jsonObject.optString("productId"));
                        orderInquiry.setProductTitle(jsonObject.optString("productTitle"));
                        orderInquiry.setPrice(jsonObject.optString("price"));
                        orderInquiry.setPieceQuantity(jsonObject.optString("pieceQuantity"));
                        orderInquiry.setOrderStatus(jsonObject.optString("orderStatus"));
                        orderInquiry.setReadStatus(jsonObject.optString("readStatus"));
                        orderInquiry.setStatus(jsonObject.optString("status"));
                        orderInquiry.setDateTimeAdded(jsonObject.optString("dateTimeAdded"));
                        orderInquiries.add(orderInquiry);
                        position++;
                    }

                    Order model = new Order(object.optString("quoteNo"), orderInquiries);
                    model.setId(object.optString("quoteId"));
                    model.setUserId(object.optString("userId"));
                    model.setCustomerId(object.optString("customerId"));
                    model.setQuoteNo(object.optString("quoteNo"));
                    model.setTotal(object.optString("total"));
                    model.setGrandTotal(object.optString("grandTotal"));
                    model.setGst(object.optString("gst"));
                    model.setCgstTotal(object.optString("cgstTotal"));
                    model.setSgstTotal(object.optString("sgstTotal"));
                    model.setPdfLink(object.optString("pdfLink"));
                    model.setPayStatus(object.optString("payStatus"));
                    model.setReadStatus(object.optString("readStatus"));
                    model.setOrderStatus(object.optString("orderStatus"));
                    model.setStatus(object.optString("status"));
                    model.setDateTimeAdded(object.optString("dateTimeAdded"));
                    model.setInquiryList(orderInquiries);
                    title = title + object.optString("quoteNo") + "\n";
                    price = price + Float.parseFloat(object.optString("grandTotal")) + "\n";
                    list.add(model);
                }

                tvProduct.setText(title);
                tvProductPrice.setText(price);
                tvCalculatedPrice.setText(Constant.priceResult.optString("totalAmount"));
                tvGSTPrice.setText(Constant.priceResult.optString("gstCharge"));
                tvTotal.setText(Constant.priceResult.optString("grandTotal"));

                tvCalculatePrice.setVisibility(View.GONE);
                llCalculatedPrice.setVisibility(View.VISIBLE);
                tvInfo.setVisibility(View.VISIBLE);

                adapter = new PriceListAdapter(context, list);

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

                for (int i = 0; i < position; i++) {
                    expandGroup(i);
                }
            } catch (Exception e) {
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
                tvCalculatePrice.setVisibility(View.GONE);
                llCalculatedPrice.setVisibility(View.VISIBLE);
                tvInfo.setVisibility(View.VISIBLE);
                e.printStackTrace();
            }
        }
    }

    private void expandGroup(int gPos) {
        if (adapter.isGroupExpanded(gPos)) {
            return;
        }
        adapter.toggleGroup(gPos);
    }
}
