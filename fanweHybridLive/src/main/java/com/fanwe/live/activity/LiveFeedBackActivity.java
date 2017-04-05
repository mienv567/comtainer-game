package com.fanwe.live.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.fanwe.hybrid.activity.BaseTitleActivity;
import com.fanwe.library.title.SDTitleItem;
import com.fanwe.library.utils.SDToast;
import com.fanwe.live.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by cheng.yuan on 2017/4/5.
 */

public class LiveFeedBackActivity extends BaseTitleActivity {

    @BindView(R.id.et_feedback)
    EditText mEtFeedback;
    @BindView(R.id.tv_word_count)
    TextView mTvWordCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_feedback);
        ButterKnife.bind(this);
        initTitle();
        initListener();
    }

    private void initTitle() {
        mTitle.setMiddleTextTop(getString(R.string.text_feedback));
        SDTitleItem itemRight = mTitle.addItemRight();
        itemRight.setImageLeft(R.drawable.ic_circled_right);
        itemRight.setTextTop(getString(R.string.send));
        itemRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SDToast.showToast(getString(R.string.send));
            }
        });
    }

    private void initListener() {
        //mEtFeedback.setFilters(new InputFilter[]{new InputFilter.LengthFilter(400)});
        mEtFeedback.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mTvWordCount.setText(String.valueOf(s.length()));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
}
