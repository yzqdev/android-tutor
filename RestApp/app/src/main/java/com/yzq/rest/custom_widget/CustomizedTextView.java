package com.yzq.rest.custom_widget;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

/**
 * Created by JDK on 2016/8/22.
 */
public class CustomizedTextView extends AppCompatTextView {
    public CustomizedTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    @Override
    protected void onFocusChanged(boolean focused, int direction,
                                  Rect previouslyFocusedRect) {
        if (focused)
            super.onFocusChanged(focused, direction, previouslyFocusedRect);
    }

    @Override
    public void onWindowFocusChanged(boolean focused) {
        if (focused)
            super.onWindowFocusChanged(focused);
    }
    @Override
    public boolean isFocused() {
        return true;
    }
}
