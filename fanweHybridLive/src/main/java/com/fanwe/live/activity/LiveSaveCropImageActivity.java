package com.fanwe.live.activity;

import android.os.Bundle;
import android.text.TextUtils;

import com.fanwe.auction.common.AuctionCommonInterface;
import com.fanwe.auction.model.App_auction_upload_imageActModel;
import com.fanwe.hybrid.activity.BaseTitleActivity;
import com.fanwe.hybrid.http.AppRequestCallback;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.fanwe.library.title.SDTitleItem;
import com.fanwe.library.utils.ImageFileCompresser;
import com.fanwe.library.utils.SDToast;
import com.fanwe.live.R;
import com.fanwe.live.appview.LiveCropImageView;
import com.fanwe.live.event.EUpLoadImageComplete;
import com.sunday.eventbus.SDEventManager;

import org.xutils.view.annotation.ViewInject;

import java.io.File;

/**
 * Created by Administrator on 2016/8/8.
 */
public class LiveSaveCropImageActivity extends BaseTitleActivity
{
    public static final String EXTRA_IMAGE_URL = "EXTRA_IMAGE_URL";

    public static final String EXTRA_TYPE = "extra_type";//0本地图片 1上传图片

    @ViewInject(R.id.crop_imageview)
    private LiveCropImageView liveCropView;

    private int extra_type;
    private String mStrUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_live_save_crop_image);
        init();
    }

    private void init()
    {
        getIntentData();
        initTitle();
        initCropView();
    }

    private void getIntentData()
    {
        extra_type = getIntent().getIntExtra(EXTRA_TYPE, 0);
        mStrUrl = getIntent().getStringExtra(EXTRA_IMAGE_URL);
        if (isEmpty(mStrUrl))
        {
            SDToast.showToast(getString(R.string.pic_not_exist));
            finish();
        }

        File file = new File(mStrUrl);
        if (!file.exists())
        {
            SDToast.showToast(getString(R.string.pic_not_exist));
            finish();
        }
        mStrUrl = "file://" + mStrUrl;
    }

    private void initTitle()
    {
        if (extra_type == 1)
        {
            mTitle.setMiddleTextTop(getString(R.string.upload_pic));
            mTitle.initRightItem(1);
            mTitle.getItemRight(0).setTextBot(getString(R.string.upload));
        } else
        {
            mTitle.setMiddleTextTop(getString(R.string.clip_pic));
            mTitle.initRightItem(1);
            mTitle.getItemRight(0).setTextBot(getString(R.string.save));
        }
    }

    private void initCropView()
    {
        liveCropView.setImageFileCompresserListener(new ImageFileCompresser.ImageFileCompresserListener()
        {
            @Override
            public void onStart()
            {
                showProgressDialog(getString(R.string.is_dispose_pic));
            }

            @Override
            public void onSuccess(File fileCompressed)
            {
                if (extra_type == 1)
                {
                    requestAuctionImageUpload(fileCompressed);
                } else
                {
                    EUpLoadImageComplete event = new EUpLoadImageComplete();
                    event.local_path = fileCompressed.getAbsolutePath();
                    String filename = fileCompressed.getName();
                    event.prefix = filename.substring(filename.lastIndexOf(".") + 1);
                    SDEventManager.post(event);
                    finish();
                }
            }

            @Override
            public void onFailure(String msg)
            {
                if (!TextUtils.isEmpty(msg))
                {
                    SDToast.showToast(msg);
                }
            }

            @Override
            public void onFinish()
            {
                dismissProgressDialog();
            }
        });
        liveCropView.loadUrl(mStrUrl);
    }

    @Override
    public void onCLickRight_SDTitleSimple(SDTitleItem v, int index)
    {
        liveCropView.clickUpload();
    }

    private void requestAuctionImageUpload(File fileCompressed)
    {
        if (fileCompressed == null)
        {
            return;
        }

        if (!fileCompressed.exists())
        {
            return;
        }

        AuctionCommonInterface.requestAuctionImageUpload(fileCompressed, new AppRequestCallback<App_auction_upload_imageActModel>()
        {
            @Override
            public void onStart()
            {
                showProgressDialog(getString(R.string.is_uploading));
            }

            @Override
            protected void onSuccess(SDResponse resp)
            {
                if (actModel.getStatus() == 1)
                {
                    EUpLoadImageComplete event = new EUpLoadImageComplete();
                    event.server_path = actModel.getServer_path();
                    event.server_full_path = actModel.getServer_full_path();
                    SDEventManager.post(event);
                    finish();
                }
            }

            @Override
            protected void onError(SDResponse resp)
            {
                super.onError(resp);
                SDToast.showToast(getString(R.string.upload_fail));
            }

            @Override
            protected void onFinish(SDResponse resp)
            {
                super.onFinish(resp);
                dismissProgressDialog();
            }
        });
    }
}
