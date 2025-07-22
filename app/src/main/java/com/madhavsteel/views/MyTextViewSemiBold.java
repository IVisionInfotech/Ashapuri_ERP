package com.madhavsteel.views;

import android.content.Context;
import android.graphics.Typeface;
import androidx.appcompat.widget.AppCompatTextView;
import android.util.AttributeSet;


public class MyTextViewSemiBold extends AppCompatTextView {

    public MyTextViewSemiBold(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public MyTextViewSemiBold(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyTextViewSemiBold(Context context) {
        super(context);
        init();
    }

    private void init() {
        if (!isInEditMode()) {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/Signika-SemiBold.ttf");
            setTypeface(tf);
        }
    }

}