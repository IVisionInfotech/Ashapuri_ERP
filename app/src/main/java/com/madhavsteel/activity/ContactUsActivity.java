package com.madhavsteel.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.cardview.widget.CardView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.madhavsteel.R;

public class ContactUsActivity extends BaseActivity {

    private TextView tvTitle;
    private ImageView ivBack;
    private CardView cvEmail, cvContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);

        tvTitle = findViewById(R.id.tvTitle);
        tvTitle.setText("Contact Us");
        ivBack = findViewById(R.id.ivBack);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        cvEmail = findViewById(R.id.cvEmail);
        cvContact = findViewById(R.id.cvContact);

        cvContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number = getResources().getString(R.string.contact);
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:" + number));
                startActivity(callIntent);
            }
        });

        cvEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent email = new Intent(Intent.ACTION_SEND);
                email.putExtra(Intent.EXTRA_EMAIL, new String[]{getResources().getString(R.string.email)});
                email.putExtra(Intent.EXTRA_SUBJECT, "");
                email.putExtra(Intent.EXTRA_TEXT, "");
                email.setType("message/rfc822");
                startActivity(Intent.createChooser(email, "Choose an Email client :"));
            }
        });
    }
}
