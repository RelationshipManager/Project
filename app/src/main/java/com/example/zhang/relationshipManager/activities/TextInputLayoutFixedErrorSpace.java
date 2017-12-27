package com.example.zhang.relationshipManager.activities;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by zhang on 2017-09-04.
 */

public class TextInputLayoutFixedErrorSpace extends TextInputLayout {

    public TextInputLayoutFixedErrorSpace(Context context) {
        super(context);
    }

    public TextInputLayoutFixedErrorSpace(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TextInputLayoutFixedErrorSpace(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setError(@Nullable CharSequence error) {
        super.setError(error);

        View layout = getChildAt(1);
        if (layout != null) {
            if (error != null && !"".equals(error.toString().trim())) {
                layout.setVisibility(VISIBLE);
            } else {
                layout.setVisibility(GONE);
            }
        }
    }
}
