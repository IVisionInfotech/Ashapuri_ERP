package com.madhavsteel.views;

import android.content.Context;
import android.graphics.Typeface;
import androidx.appcompat.widget.AppCompatRadioButton;
import android.util.AttributeSet;


public class MyRadioButton extends AppCompatRadioButton {

    public MyRadioButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public MyRadioButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyRadioButton(Context context) {
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