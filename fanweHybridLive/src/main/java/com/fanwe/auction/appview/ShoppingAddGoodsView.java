package com.fanwe.auction.appview;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.fanwe.auction.adapter.ItemImgAdapter;
import com.fanwe.shopping.common.ShoppingCommonInterface;
import com.fanwe.shopping.model.App_shop_goodsActModel;
import com.fanwe.auction.model.ImageModel;
import com.fanwe.hybrid.http.AppRequestCallback;
import com.fanwe.library.adapter.SDAdapter;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.fanwe.library.event.EOnActivityResult;
import com.fanwe.library.handler.PhotoHandler;
import com.fanwe.library.utils.SDJsonUtil;
import com.fanwe.library.utils.SDToast;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.live.R;
import com.fanwe.live.activity.LiveSaveCropImageActivity;
import com.fanwe.live.dao.UserModelDao;
import com.fanwe.live.event.EUpLoadImageComplete;
import com.fanwe.live.utils.PhotoBotShowUtils;
import com.fanwe.shopping.model.ShopMystoreListItemModel;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by shibx on 2016/9/19.
 */
public class ShoppingAddGoodsView extends AuctionGoodsBaseView implements TextView.OnEditorActionListener,TextWatcher{

    private LinearLayout ll_layout_img;

    private EditText et_goods_name;
    private EditText et_goods_price;
    private EditText et_kd_cost;
    private EditText et_goods_detail_url;
    private EditText et_goods_des;

    private List<ImageModel> mListImage;
    private ItemImgAdapter mAdapterImg;
    private PhotoHandler mHandler;
    private Map<String,String> mapPic;//图片

    private int mPicNum;//图片数量
    private boolean isFull;//已添加图片是否等于可添加数量

    private String user_id;

    private ShopMystoreListItemModel mModel;

    public ShoppingAddGoodsView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public ShoppingAddGoodsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ShoppingAddGoodsView(Context context,ShopMystoreListItemModel model) {
        super(context);
        this.mModel = model;
        init();
    }

    @Override
    protected void baseConstructorInit() {
        super.baseConstructorInit();
        user_id = UserModelDao.query().getUserId();
        mPicNum = 4;
        isFull = false;
        mapPic = new HashMap<>();
        mListImage = new ArrayList<>();
        mHandler = new PhotoHandler((FragmentActivity) getActivity());
        mAdapterImg = new ItemImgAdapter(mListImage,getActivity());
    }

    @Override
    protected void init() {
        super.init();
        setContentView(R.layout.view_auction_add_goods);
        initView();
    }

    private void initView() {
        initTitle(R.id.title);
        ll_layout_img = find(R.id.ll_layout_img);
        et_goods_name = find(R.id.et_goods_name);
        et_goods_price = find(R.id.et_goods_price);
        et_kd_cost = find(R.id.et_kd_cost);
        et_goods_detail_url = find(R.id.et_goods_detail_url);
        et_goods_des = find(R.id.et_goods_des);

        et_goods_name.setOnEditorActionListener(this);
        et_goods_price.setOnEditorActionListener(this);
        et_kd_cost.setOnEditorActionListener(this);
        et_goods_price.addTextChangedListener(this);
        et_kd_cost.addTextChangedListener(this);
        et_goods_des.setOnEditorActionListener(this);
        if(mModel != null) {
            for (String url: mModel.getImgs()) {
                mListImage.add(new ImageModel(url));
                mapPic.put(url,url);
            }
            et_goods_name.setText(mModel.getName());
            et_goods_price.setText(mModel.getPrice());
            et_kd_cost.setText(mModel.getKd_cost());
            et_goods_detail_url.setText(mModel.getUrl());
            et_goods_des.setText(mModel.getDescription());
        }
        initGoodsImage();
    }

    private void initGoodsImage() {
        mListImage.add(new ImageModel(null));
        mAdapterImg.setOnItemRemoveListener(new ItemImgAdapter.OnItemRemoveListener() {
            @Override
            public void onRemove(int position, ImageModel item, View view) {
                if(isFull) {
                    mListImage.set(mListImage.size()-1,new ImageModel(null));
                    isFull = false;
                } else if(mListImage.size() == 1) {
                    mListImage.set(0,new ImageModel(null));
                } else{
                    mListImage.remove(position);
                }
                //移除参数内的该图片
                if(mapPic.containsKey(item.getUri())) {
                    mapPic.remove(item.getUri());
                }
                mAdapterImg.notifyDataSetChanged();
                getAllGoodsImage();
            }
        });
        mAdapterImg.setItemClickListener(new SDAdapter.ItemClickListener<ImageModel>() {
            @Override
            public void onClick(int position, ImageModel item, View view) {
                PhotoBotShowUtils.openBotPhotoView(getActivity(),mHandler,PhotoBotShowUtils.DIALOG_ALBUM);
            }
        });
        getAllGoodsImage();
        mHandler.setListener(new PhotoHandler.PhotoHandlerListener() {
            @Override
            public void onResultFromAlbum(File file) {
                openCropAct(file);
            }

            @Override
            public void onResultFromCamera(File file) {
                openCropAct(file);
            }

            @Override
            public void onFailure(String msg) {
            }
        });
    }

    /**
     * 获取所有图片
     */
    private void getAllGoodsImage() {
        ll_layout_img.removeAllViews();
        for (int i =0 ; i < mListImage.size() ; i++) {
            View view = mAdapterImg.getView(i,null,ll_layout_img);
            if(view != null) {
                ll_layout_img.addView(view);
            }
        }
    }

    private void openCropAct(File file) {
        Intent intent = new Intent(getActivity(),LiveSaveCropImageActivity.class);
        intent.putExtra(LiveSaveCropImageActivity.EXTRA_IMAGE_URL,file.getAbsolutePath());
        intent.putExtra(LiveSaveCropImageActivity.EXTRA_TYPE,1);
        getActivity().startActivity(intent);
    }

    /**
     * 接收所在activity的OnActivityResult事件
     * @param event
     */
    @Override
    public void onEventMainThread(EOnActivityResult event) {
        super.onEventMainThread(event);
        if(event.activity == getActivity()) {
            mHandler.onActivityResult(event.requestCode,event.resultCode,event.data);
        }
    }

    /**
     * 接收图片选择回传地址事件
     * @param event
     */
    public void onEventMainThread(EUpLoadImageComplete event) {
        if(!TextUtils.isEmpty(event.server_full_path)) {
            mapPic.put(event.server_full_path,event.server_full_path);

            if(mListImage.size() == mPicNum) {
                mListImage.set(mListImage.size()-1,new ImageModel(event.server_full_path));
                isFull = true;
            } else {
                mListImage.add(mListImage.size()-1,new ImageModel(event.server_full_path));
            }
            mAdapterImg.notifyDataSetChanged();
            getAllGoodsImage();
        }
    }

    @Override
    protected void initTitleText() {
        if(mModel == null) {
            setTitleText("新增购物商品","新增");
        } else {
            setTitleText("编辑购物商品","编辑");
        }

    }

    private void setTitleText(String titleText,String btnText) {
        mTitle.setMiddleTextTop(titleText);
        mTitle.getItemRight(0).setTextBot(btnText);
    }

    @Override
    protected void clickTitleLeft() {
        if(mModel == null) {
            showDialog("是否放弃新增购物商品？","确定","取消");
        } else {
            showDialog("是否放弃编辑购物商品？","确定","取消");
        }

    }

    @Override
    protected void clickTitleMid() {

    }

    @Override
    protected void clickTitleRight() {
        if(mModel == null) {
            requestAddGoods();
        } else {
            requestEditGoods();
        }

    }

    @Override
    protected void clickDialogConfirm() {
        getActivity().finish();
    }

    @Override
    protected boolean isParamsComplete() {
        if(mapPic.size() == 0) {
            SDToast.showToast("请添加商品图片");
            return false;
        }
        if(isEtEmpty(et_goods_name)) {
            SDToast.showToast("请填写商品名称");
            return false;
        }
        if(isEtEmpty(et_goods_price)) {
            SDToast.showToast("请添加商品价格");
            return false;
        }
        if(isEtEmpty(et_kd_cost)) {
            SDToast.showToast("请添加快递价格");
            return false;
        }
        if(isEtEmpty(et_goods_detail_url)) {
            SDToast.showToast("请添加商品链接地址");
            return false;
        }

        return true;
    }

    private void requestAddGoods() {
        //检查参数完整
        if(!isParamsComplete()) {
            return ;
        }

        requestShopAddGoods();
    }

    private void requestEditGoods() {
        //检查参数完整
        if(!isParamsComplete()) {
            return ;
        }
        requestShopEditGoods();
    }


    private void requestShopAddGoods() {
        showProgressDialog("正在新增商品");
        ShoppingCommonInterface.requestShopAddGoods(user_id, getEtContent(et_goods_name), SDJsonUtil.object2Json(mapPic), Float.valueOf(getEtContent(et_goods_price)), Float.valueOf(getEtContent(et_kd_cost)), getEtContent(et_goods_detail_url), getEtContent(et_goods_des), new AppRequestCallback<App_shop_goodsActModel>() {
            @Override
            protected void onSuccess(SDResponse sdResponse) {
                if(rootModel.isOk()) {
                    SDToast.showToast("发布商品成功！");
                    getActivity().finish();
                }
            }

            @Override
            protected void onFinish(SDResponse resp) {
                super.onFinish(resp);
                dismissProgressDialog();
            }
        });
    }

    private void requestShopEditGoods() {
        showProgressDialog("正在编辑商品");
        ShoppingCommonInterface.requestShopEditGoods(mModel.getId(),user_id, getEtContent(et_goods_name), SDJsonUtil.object2Json(mapPic), Float.valueOf(getEtContent(et_goods_price)), Float.valueOf(getEtContent(et_kd_cost)), getEtContent(et_goods_detail_url), getEtContent(et_goods_des), new AppRequestCallback<App_shop_goodsActModel>() {
            @Override
            protected void onSuccess(SDResponse sdResponse) {
                if(rootModel.isOk()) {
                    SDToast.showToast("编辑商品成功！");
                    getActivity().finish();
                }
            }

            @Override
            protected void onFinish(SDResponse resp) {
                super.onFinish(resp);
                dismissProgressDialog();
            }
        });
    }


    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        SDViewUtil.hideInputMethod(v);
        if(event == null) {
            return true;
        }
        return (event.getKeyCode() == KeyEvent.KEYCODE_ENTER);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        String string = s.toString();
        if(string.contains(".")) {
            int index = string.indexOf(".");
            if(string.substring(index + 1).length() > 2) {
                SDToast.showToast("超过两位小数");
            };
        }
    }
}
