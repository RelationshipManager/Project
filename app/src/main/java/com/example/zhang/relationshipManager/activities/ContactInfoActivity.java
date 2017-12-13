package com.example.zhang.relationshipManager.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.zhang.relationshipManager.R;

import butterknife.BindView;

/**
 * Created by 29110 on 2017/12/12.
 */

public class ContactInfoActivity extends BaseActivity{
    @BindView(R.id.contact_name)
    TextView mTextView;
    @BindView(R.id.contact_image)
    ImageView mImageView;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
}
