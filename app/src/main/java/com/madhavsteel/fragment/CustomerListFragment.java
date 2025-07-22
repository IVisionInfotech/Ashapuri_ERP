package com.madhavsteel.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.franmontiel.fullscreendialog.FullScreenDialogContent;
import com.franmontiel.fullscreendialog.FullScreenDialogController;
import com.madhavsteel.R;
import com.madhavsteel.adapter.CustomerListAdapter;
import com.madhavsteel.model.Customer;
import com.madhavsteel.utils.Constant;
import com.madhavsteel.utils.MyApplication;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomerListFragment extends Fragment implements FullScreenDialogContent {

    private static final String TAG = "CustomerListFragment";
    private Context context;
    private List<Customer> list = new ArrayList<>();
    private EditText etSearchHere;
    private RecyclerView recyclerView;
    private CustomerListAdapter adapter;
    private FullScreenDialogController dialogController;
    private ProgressDialog pDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_recyclerview_list, container, false);

        context = getActivity();

        init(view);

        if (Constant.customerArray == null) {
            getCustomer();
        } else {
            if (Constant.customerArray.length() > 0) {
                bindData();
            } else {
                getCustomer();
            }
        }

        return view;
    }

    private void init(View view) {

        recyclerView = view.findViewById(R.id.recyclerView);
        etSearchHere = view.findViewById(R.id.etSearchHere);

        etSearchHere.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(etSearchHere.getWindowToken(), 0);
                    return true;
                }
                return false;
            }
        });

        etSearchHere.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (adapter != null) {
                    if (!s.toString().isEmpty()) {
                        adapter.getFilter().filter(s);
                    }
                }
            }
        });
    }

    private void bindRecyclerView() {

        GridLayoutManager mLayoutManager = new GridLayoutManager(context, 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setHasFixedSize(false);

        adapter = new CustomerListAdapter(getActivity(), list, new CustomerListAdapter.ClickListener() {
            @Override
            public void onItemSelected(Customer model) {
                Constant.customerId = model.getId();
                Constant.customerName = model.getName();
                Bundle result = new Bundle();
                result.putString("result1", "true");
                dialogController.confirm(result);
            }
        });

        recyclerView.setAdapter(adapter);
    }

    private void getCustomer() {

        showProgressDialog(context, "Please wait..");
        StringRequest strReq = new StringRequest(Request.Method.POST, Constant.getCustomer, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e(TAG, response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int status = jsonObject.optInt("status");
                    if (status == 1) {
                        Constant.customerArray = jsonObject.optJSONArray("result");
                        bindData();
                    } else {
                        if (adapter != null) {
                            list.clear();
                            adapter.notifyDataSetChanged();
                        }
                        Toast.makeText(context, jsonObject.optString("message"), Toast.LENGTH_SHORT).show();
                    }
                    hideProgressDialog();
                } catch (JSONException e) {
                    e.printStackTrace();
                    hideProgressDialog();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Log.d(TAG, "Error: " + error.getMessage());
                hideProgressDialog();
            }
        }) {

            /*@Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("userId", userId);
                return params;
            }*/

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<String, String>();
                String auth = "Basic " + Base64.encodeToString(Constant.credentials.getBytes(), Base64.NO_WRAP);
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                headers.put("Authorization", auth);
                return headers;
            }
        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(0, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MyApplication.getInstance().addToRequestQueue(strReq);
    }

    private void bindData() {
        list.clear();
        try {
            for (int i = 0; i < Constant.customerArray.length(); i++) {
                JSONObject jsonObject = Constant.customerArray.optJSONObject(i);
                Customer model = new Customer();
                model.setId(jsonObject.optString("id"));
                model.setName(jsonObject.optString("name"));
                model.setContact(jsonObject.optString("contact"));
                model.setEmail(jsonObject.optString("email"));
                model.setAddress(jsonObject.optString("address"));
                model.setcName(jsonObject.optString("cName"));
                model.setGstNo(jsonObject.optString("gstNo"));
                model.setCity(jsonObject.optString("city"));
                model.setState(jsonObject.optString("state"));
                model.setDateTimeAdded(jsonObject.optString("dateTimeAdded"));
                list.add(model);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        bindRecyclerView();
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

    @Override
    public void onDialogCreated(FullScreenDialogController dialogController) {
        this.dialogController = dialogController;
    }

    @Override
    public boolean onConfirmClick(FullScreenDialogController dialogController) {
        Bundle result = new Bundle();
        dialogController.confirm(result);
        return true;
    }

    @Override
    public boolean onDiscardClick(FullScreenDialogController dialogController) {
        dialogController.discard();
        return true;
    }
}