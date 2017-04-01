package com.qy.ysys.yishengyishi.views;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.orhanobut.logger.Logger;
import com.qy.ysys.yishengyishi.R;
import com.qy.ysys.yishengyishi.R2;
import com.qy.ysys.yishengyishi.adapter.MemoirInfoAdapter;
import com.qy.ysys.yishengyishi.event.ENeedRefreshMemoirs;
import com.qy.ysys.yishengyishi.httputils.RequestInterface;
import com.qy.ysys.yishengyishi.model.BaseModel;
import com.qy.ysys.yishengyishi.model.MemoirInfoModel;
import com.qy.ysys.yishengyishi.views.customviews.BaseTitleActivity;
import com.qy.ysys.yishengyishi.views.customviews.ITitleBarOnClickListener;
import com.qy.ysys.yishengyishi.views.customviews.ITitleView;
import com.qy.ysys.yishengyishi.widgets.SDRecyclerView;
import com.qy.ysys.yishengyishi.widgets.dialog.WeiboDialogUtils;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
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
public class MemoirInfoActivity extends BaseTitleActivity {
    public static final String EXTRA_FM_ID = "extra_fm_id";
    public static final String EXTRA_COVER_IMG = "extra_cover_img";
    private static final int MAX_COUNT = 9;
    private static final int REQUEST_IMAGE = 2;
    @BindView(R2.id.sv_root)
    ScrollView sv_root;
    @BindView(R2.id.recyclerView)
    SDRecyclerView recyclerView;
    @BindView(R2.id.iv_cover)
    ImageView mCoverImg;
    @BindView(R2.id.tv_upload)
    TextView mUploadTxt;
    private MemoirInfoAdapter mAdapter;
    private int mFmId;
    private String mCoverImgUrl;
    private ArrayList<String> mSelectPath = new ArrayList<>();
    private ArrayList<String> mCompressImgPath = new ArrayList<>();
    @Override
    protected void initTitleBar(ITitleView titleView) {
        titleView.setTitleText("我的相册");
        titleView.setLeftImage(R.mipmap.ic_back);
        titleView.setTitleBarOnClickListener(new ITitleBarOnClickListener() {
            @Override
            public void onClickLeft() {
                finish();
            }

            @Override
            public void onClickRight() {

            }
        });
    }
    @Override
    protected int setContentViewByLayoutID() {
        return R.layout.activity_memoir_info;
    }

    @Override
    protected void afterOnCreate() {
        super.afterOnCreate();
        initIntent();
        initRecycleView();
        initListenr();
    }

    private void initListenr(){
        mUploadTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MemoirInfoActivityPermissionsDispatcher.pickImageWithCheck(MemoirInfoActivity.this);
            }
        });
    }



    private void doUpload(){
        final Dialog dialog = WeiboDialogUtils.createLoadingDialog(MemoirInfoActivity.this, "正在处理……");
        compressAllImgs();
        RequestInterface.uploadMemoir(mFmId, RequestInterface.MEMOIR_IS_FAMILY_ZONE, mCompressImgPath, new Callback<BaseModel>() {
            @Override
            public void onResponse(Call<BaseModel> call, Response<BaseModel> response) {
                if(response.body().isSuccess()){
                    requestData();
                }
                EventBus.getDefault().post(new ENeedRefreshMemoirs());
                sv_root.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        sv_root.fullScroll(ScrollView.FOCUS_DOWN);
                    }
                }, 100);
                WeiboDialogUtils.closeDialog(dialog);
            }

            @Override
            public void onFailure(Call<BaseModel> call, Throwable t) {
                WeiboDialogUtils.closeDialog(dialog);
            }
        });
    }

    private void compressAllImgs(){
        if(mSelectPath != null && mSelectPath.size() > 0){
            for(String path : mSelectPath){
                File compressFile = Compressor.getDefault(MemoirInfoActivity.this).compressToFile(new File(path));
                String compressFilePath = compressFile.getAbsolutePath();
                mCompressImgPath.add(compressFilePath);
            }
        }
    }

    private void initRecycleView(){
        mAdapter = new MemoirInfoAdapter(MemoirInfoActivity.this);
        recyclerView.setLayoutManager(new GridLayoutManager(MemoirInfoActivity.this, 2){
            //为了让scrollview变得顺滑
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        recyclerView.setAdapter(mAdapter);
    }

    private void initIntent(){
        Intent intent = getIntent();
        mFmId = intent.getIntExtra(EXTRA_FM_ID,0);
        mCoverImgUrl = intent.getStringExtra(EXTRA_COVER_IMG);
        Glide.with(MemoirInfoActivity.this).load(mCoverImgUrl).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.color.bg_no_photo).into(mCoverImg);
    }

    @Override
    public void onClick(View v) {

    }

    private void requestData(){
        RequestInterface.queryFamilyMemoirInfo(mFmId, new Callback<MemoirInfoModel>() {
            @Override
            public void onResponse(Call<MemoirInfoModel> call, Response<MemoirInfoModel> response) {
                if (response.body().isSuccess()) {
                    if (response.body().getReturnObj() != null) {
                        //告诉服务器 封面不存在直接返回空字符串就好了
                        if (TextUtils.isEmpty(mCoverImgUrl) || mCoverImgUrl.contains("null")) {
                            mCoverImgUrl = response.body().getReturnObj().getCoverPath();
                            Glide.with(MemoirInfoActivity.this).load(mCoverImgUrl).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.color.bg_no_photo).into(mCoverImg);
                        }
                        mAdapter.setDatas(response.body().getReturnObj().getImages());
                        mAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(Call<MemoirInfoModel> call, Throwable t) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        requestData();
    }


    @NeedsPermission({Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.CAMERA})
    void pickImage(){
        MultiImageSelector selector = MultiImageSelector.create(MemoirInfoActivity.this);
        selector.showCamera(false);
        selector.count(MAX_COUNT);
        selector.multi();
        selector.origin(mSelectPath);
        selector.start(MemoirInfoActivity.this, REQUEST_IMAGE);
    }

    @OnShowRationale({Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.CAMERA})
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

    @OnPermissionDenied({Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.CAMERA})
    void showDeniedForCamera() {
        Toast.makeText(this, "权限被拒绝", Toast.LENGTH_SHORT).show();
    }

    @OnNeverAskAgain({Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.CAMERA})
    void showNeverAskForCamera() {
        Toast.makeText(this, "不再询问读写权限、摄像头权限", Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        MemoirInfoActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_IMAGE){
            if(resultCode == RESULT_OK){
                mSelectPath = data.getStringArrayListExtra(MultiImageSelector.EXTRA_RESULT);
                StringBuilder sb = new StringBuilder();
                for(String p: mSelectPath){
                    sb.append(p);
                    sb.append("\n");
                }
                Logger.d(sb.toString());
                doUpload();
            }
        }
    }
}
