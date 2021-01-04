package com.yhy.librarysystem;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.widget.TextView;


public class MyTextView extends android.support.v7.widget.AppCompatTextView {
    //  默认不选中
    public boolean isSelected=true;

    public MyTextView(Context context) {
        super(context);
    }


}