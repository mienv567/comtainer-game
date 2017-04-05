package com.fanwe.live.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fanwe.hybrid.activity.BaseTitleActivity;
import com.fanwe.library.utils.LogUtil;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.view.CircleImageView;
import com.fanwe.live.R;
import com.fanwe.live.model.PlayerIntroductionModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PlayerInfoActivity extends BaseTitleActivity {

    @BindView(R.id.iv_player_head)
    CircleImageView mIvPlayerHead;
    @BindView(R.id.tv_player_name)
    TextView mTvPlayerName;
    @BindView(R.id.tv_player_introduce)
    TextView mTvPlayerIntroduce;
    @BindView(R.id.iv_player_gender)
    ImageView mIvPlayerGender;
    @BindView(R.id.tv_video_introduce)
    TextView mTvVideoIntroduce;
    @BindView(R.id.tv_living_environment)
    TextView mTvLivingEnvironment;
    @BindView(R.id.tv_task_progress)
    TextView mTvTaskProgress;
    @BindView(R.id.tv_first_fans)
    TextView mTvFirstFans;
    @BindView(R.id.tv_charm_score)
    TextView mTvCharmScore;
    @BindView(R.id.tv_like_count)
    TextView mTvLikeCount;
    private PlayerIntroductionModel mPlayerInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_info);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        mTitle.setMiddleTextTop(getString(R.string.player_introduction));
        mPlayerInfo = (PlayerIntroductionModel) getIntent().getSerializableExtra("player_info");
        LogUtil.d(mPlayerInfo.toString());

        SDViewBinder.setImageView(this, mIvPlayerHead, mPlayerInfo.headImage);
        SDViewBinder.setTextView(mTvPlayerName, mPlayerInfo.nickName);
        SDViewBinder.setTextView(mTvPlayerIntroduce, mPlayerInfo.introduce);
        if (mPlayerInfo.sex == 1) {
            SDViewBinder.setImageViewResource(mIvPlayerGender, R.drawable.ic_male_bg_blue, false);
        } else {
            SDViewBinder.setImageViewResource(mIvPlayerGender, R.drawable.ic_female_bg_red, false);
        }
    }

    @OnClick({R.id.tv_video_introduce, R.id.tv_living_environment, R.id.tv_task_progress,
            R.id.tv_first_fans, R.id.tv_charm_score, R.id.tv_like_count})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_video_introduce:
                break;
            case R.id.tv_living_environment:
                break;
            case R.id.tv_task_progress:
                break;
            case R.id.tv_first_fans:
                break;
            case R.id.tv_charm_score:
                break;
            case R.id.tv_like_count:
                break;
        }
    }
}
