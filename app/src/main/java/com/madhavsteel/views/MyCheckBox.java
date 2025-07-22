package com.madhavsteel.views;

import android.content.Context;
import android.graphics.Typeface;
import androidx.appcompat.widget.AppCompatCheckBox;
import android.util.AttributeSet;


public class MyCheckBox extends AppCompatCheckBox {

    public MyCheckBox(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public MyCheckBox(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyCheckBox(Context context) {
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