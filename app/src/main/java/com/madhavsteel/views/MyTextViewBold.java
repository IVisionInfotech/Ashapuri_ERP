package com.madhavsteel.views;

import android.content.Context;
import android.graphics.Typeface;
import androidx.appcompat.widget.AppCompatTextView;
import android.util.AttributeSet;


public class MyTextViewBold extends AppCompatTextView {

    public MyTextViewBold(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public MyTextViewBold(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyTextViewBold(Context context) {
        super(context);
        init();
    }

    private void init() {
        if (!isInEditMode()) {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/Signika-Bold.ttf");
            setTypeface(tf);
        }
    }

}