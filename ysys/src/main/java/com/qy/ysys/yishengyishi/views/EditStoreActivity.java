package com.qy.ysys.yishengyishi.views;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.qy.ysys.yishengyishi.R;
import com.qy.ysys.yishengyishi.R2;
import com.qy.ysys.yishengyishi.event.ENeedRefreshFamily;
import com.qy.ysys.yishengyishi.httputils.RequestInterface;
import com.qy.ysys.yishengyishi.model.BaseModel;
import com.qy.ysys.yishengyishi.utils.Base64Util;
import com.qy.ysys.yishengyishi.utils.ToastUtil;
import com.qy.ysys.yishengyishi.views.customviews.BaseTitleActivity;
import com.qy.ysys.yishengyishi.views.customviews.ITitleBarOnClickListener;
import com.qy.ysys.yishengyishi.views.customviews.ITitleView;
import com.qy.ysys.yishengyishi.widgets.MultiImageView;
import com.qy.ysys.yishengyishi.widgets.dialog.WeiboDialogUtils;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import id.zelory.compressor.Compressor;
import me.nereo.multi_image_selector.MultiImageSelector;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@RuntimePermissions
public class EditStoreActivity extends BaseTitleActivity {
    private static final int DO_NOT_SYNCHRONOUS = 0;
    private static final int DO_SYNCHRONOUS = 1;
    private static final int TYPE_FAMILY_CRICLE = 1;
    private static final int TYPE_PHOTO = 1;
    private static final int TYPE_GALLERY = 2;
    private static final int REQUEST_IMAGE = 2;
    private static final int MAX_COUNT = 9;
    protected static final int REQUEST_STORAGE_READ_ACCESS_PERMISSION = 101;
    @BindView(R2.id.tv_editstore_save)
    View tv_save;
    @BindView(R2.id.iv_take_photo)
    ImageView mTakePhoto;
    @BindView(R2.id.iv_gallery)
    ImageView mGallery;
    @BindView(R2.id.multiImagView)
    MultiImageView mMultiImageView;
    @BindView(R2.id.et_content)
    EditText mContent;
    private int mType = TYPE_PHOTO;
    private ArrayList<String> mSelectPath = new ArrayList<>();
    private ArrayList<String> mSelectShowPath = new ArrayList<>();
    private ArrayList<String> mCompressImgPath = new ArrayList<>();

    @Override
    protected void initTitleBar(ITitleView titleView) {
        titleView.setTitleText("发布箱吧");
        titleView.setLeftImage(R.mipmap.ic_back);
        titleView.setTitleBarOnClickListener(new ITitleBarOnClickListener() {
            @Override
            public void onClickLeft() {
                EditStoreActivity.this.finish();
            }

            @Override
            public void onClickRight() {

            }
        });
    }

    @Override
    protected void beforeOnCreate(Bundle savedInstanceState) {
        super.beforeOnCreate(savedInstanceState);

    }

    @Override
    protected int setContentViewByLayoutID() {
        return R.layout.act_editstore;
    }

    @Override
    protected void initView(View contentView) {
        super.initView(contentView);
        tv_save.setOnClickListener(this);
        mTakePhoto.setOnClickListener(this);
        mGallery.setOnClickListener(this);
        mMultiImageView.setRunOriginal(true);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.tv_editstore_save) {
            publishFamilyZone();
        } else if (id == R.id.iv_take_photo) {
            mType = TYPE_PHOTO;
            EditStoreActivityPermissionsDispatcher.pickImageWithCheck(this);
//                pickImage();
        } else if (id == R.id.iv_gallery) {
            mType = TYPE_GALLERY;
            EditStoreActivityPermissionsDispatcher.pickImageWithCheck(this);
//                pickImage();
        }

    }

    protected void publishFamilyZone() {
        if (mSelectPath != null && mSelectPath.size() > 0) {
            compressAllImgs();
        }
        final Dialog dialog = WeiboDialogUtils.createLoadingDialog(EditStoreActivity.this, "正在处理……");
        String content = (mContent.getText() + "").replace(" ", "");
//        content = Base64Util.getBASE64(content);
//        String city = Base64Util.getBASE64("长沙");
        String city = "长沙";
        RequestInterface.publicFamilyZonePhoto(content, city, System.currentTimeMillis(), null, TYPE_FAMILY_CRICLE, null, DO_NOT_SYNCHRONOUS, mCompressImgPath,
                new Callback<BaseModel>() {
                    @Override
                    public void onResponse(Call<BaseModel> call, Response<BaseModel> response) {
                        Logger.d(response.body());
                        if (response.body().isSuccess()) {
                            EventBus.getDefault().post(new ENeedRefreshFamily());
                            WeiboDialogUtils.closeDialog(dialog);
                            finish();
                        }
                    }

                    @Override
                    public void onFailure(Call<BaseModel> call, Throwable t) {
                        Logger.d(t.getMessage());
                    }
                });
    }

    private void compressAllImgs() {
        if (mSelectPath != null && mSelectPath.size() > 0) {
            for (String path : mSelectPath) {
                File compressFile = Compressor.getDefault(EditStoreActivity.this).compressToFile(new File(path));
                String compressFilePath = compressFile.getAbsolutePath();
                mCompressImgPath.add(compressFilePath);
            }
        }
    }

    @NeedsPermission({Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA})
    void pickImage() {
        if (mType == TYPE_PHOTO) {
            MultiImageSelector selector = MultiImageSelector.create(EditStoreActivity.this);
            selector.showCamera(true);
            selector.single();
            selector.origin(mSelectPath);
            selector.start(EditStoreActivity.this, REQUEST_IMAGE);
        } else {
            MultiImageSelector selector = MultiImageSelector.create(EditStoreActivity.this);
            selector.showCamera(true);
            selector.count(MAX_COUNT);
            selector.multi();
            selector.origin(mSelectPath);
            selector.start(EditStoreActivity.this, REQUEST_IMAGE);
        }
    }

    @OnShowRationale({Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA})
    void showRationaleForStorage(final PermissionRequest request) {
        new AlertDialog.Builder(this)
                .setMessage("申请读写权限、申请摄像头权限")
                .setPositiveButton("允许", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        request.proceed();
                    }
                })
                .setNegativeButton("禁止", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        request.cancel();
                    }
                })
                .show();
    }

    @OnPermissionDenied({Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA})
    void showDeniedForCamera() {
        Toast.makeText(this, "权限被拒绝", Toast.LENGTH_SHORT).show();
    }

    @OnNeverAskAgain({Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA})
    void showNeverAskForCamera() {
        Toast.makeText(this, "不再询问读写权限、摄像头权限", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // NOTE: delegate the permission handling to generated method
        EditStoreActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == RESULT_OK) {
                mSelectPath = data.getStringArrayListExtra(MultiImageSelector.EXTRA_RESULT);
                StringBuilder sb = new StringBuilder();
                for (String p : mSelectPath) {
                    sb.append(p);
                    sb.append("\n");
                }
                Logger.d(sb.toString());
                setNineImages();
            }
        }
    }

    private void setNineImages() {
        convertPathsToShowPaths();
        mMultiImageView.setList(mSelectShowPath);
    }

    private void convertPathsToShowPaths() {
        if (mSelectPath.size() > 0) {
            mSelectShowPath.clear();
            for (String str : mSelectPath) {
                mSelectShowPath.add("file://" + str);
            }
        }
    }

}
