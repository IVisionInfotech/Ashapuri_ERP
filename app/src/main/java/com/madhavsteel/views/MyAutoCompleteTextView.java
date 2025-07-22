package com.madhavsteel.views;

import android.content.Context;
import android.graphics.Typeface;
import androidx.appcompat.widget.AppCompatAutoCompleteTextView;
import android.util.AttributeSet;


public class MyAutoCompleteTextView extends AppCompatAutoCompleteTextView {

    public MyAutoCompleteTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public MyAutoCompleteTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyAutoCompleteTextView(Context context) {
        super(context);
        init();
    }

    private void init() {
        if (!isInEditMode()) {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/Signika-Regular.ttf");
            setTypeface(tf);
        }
    }

}