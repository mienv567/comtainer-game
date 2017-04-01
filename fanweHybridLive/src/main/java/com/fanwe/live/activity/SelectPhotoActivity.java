package com.fanwe.live.activity;

import android.os.Bundle;

import com.fanwe.hybrid.activity.BaseTitleActivity;
import com.fanwe.library.common.SDSelectManager;
import com.fanwe.library.model.ImageModel;
import com.fanwe.library.title.SDTitleItem;
import com.fanwe.library.utils.SDToast;
import com.fanwe.live.R;
import com.fanwe.live.appview.SDSelectImageView;
import com.fanwe.live.event.ESelectImage;
import com.sunday.eventbus.SDEventManager;

import org.xutils.view.annotation.ViewInject;

/**
 * Created by Administrator on 2016/9/1.
 */
public class SelectPhotoActivity extends BaseTitleActivity
{
    @ViewInject(R.id.view_select_image)
    private SDSelectImageView view_select_image;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_select_photo);

        mTitle.setMiddleTextTop(getString(R.string.pic));
        mTitle.initRightItem(1);
        mTitle.getItemRight(0).setTextBot(getString(R.string.send));

        view_select_image.setSelectListener(defaultSelectListener);
        view_select_image.loadImage();
    }

    private SDSelectManager.SDSelectManagerListener<ImageModel> defaultSelectListener = new SDSelectManager.SDSelectManagerListener<ImageModel>()
    {
        @Override
        public void onNormal(int index, ImageModel item)
        {

        }

        @Override
        public void onSelected(int index, ImageModel item)
        {

        }
    };

    @Override
    public void onCLickRight_SDTitleSimple(SDTitleItem v, int index)
    {
        ImageModel model = view_select_image.getSelectManager().getSelectedItem();

        if (model == null)
        {
            SDToast.showToast(getString(R.string.please_choose_pic));
        } else
        {
            ESelectImage event = new ESelectImage();
            event.listImage.add(model);
            SDEventManager.post(event);
            finish();
        }
    }
}
