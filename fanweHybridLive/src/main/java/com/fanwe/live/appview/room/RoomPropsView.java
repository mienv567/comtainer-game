package com.fanwe.live.appview.room;

import android.content.Context;
import android.support.annotation.IdRes;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.fanwe.hybrid.http.AppRequestCallback;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.fanwe.library.utils.LogUtil;
import com.fanwe.library.utils.SDAnimationUtil;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.library.view.SDRecyclerView;
import com.fanwe.live.R;
import com.fanwe.live.adapter.LivePropsRecyclerAdapter;
import com.fanwe.live.common.CommonInterface;
import com.fanwe.live.model.GoodsDetailModel;
import com.fanwe.live.model.GoodsListModel;
import com.fanwe.live.model.GoodsTypeModel;

import java.util.List;


/**
 * Created by Yuan on 2017/3/18.
 * 邮箱：44004606@qq.com
 */

public class RoomPropsView extends RoomView implements RadioGroup.OnCheckedChangeListener {

    private RadioGroup radio_group_props;
    private SDRecyclerView rv_goods_list;
    private EditText et_search_goods;

    private LivePropsRecyclerAdapter mAdapter;
    private List<GoodsDetailModel> taskGoods;
    private List<GoodsDetailModel> lifeGoods;

    public RoomPropsView(Context context) {
        super(context);
    }

    @Override
    protected int onCreateContentView() {
        return R.layout.view_live_props;
    }

    @Override
    protected void baseConstructorInit() {
        super.baseConstructorInit();
        radio_group_props = find(R.id.radio_group_props);
        rv_goods_list = find(R.id.rv_task_list);
        et_search_goods = find(R.id.et_search_goods);
        register();
    }

    private void register() {
        radio_group_props.setOnCheckedChangeListener(this);
        mAdapter = new LivePropsRecyclerAdapter(getActivity());
        rv_goods_list.setAdapter(mAdapter);
        if (getLiveInfo().isCreater()) {
            requestGoods();
        }
        et_search_goods.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    SDViewUtil.hideInputMethod(et_search_goods);
                }
                return false;
            }
        });
    }

    private void requestGoods() {
        CommonInterface.requestGoodsList(new AppRequestCallback<GoodsListModel>() {
            @Override
            protected void onSuccess(SDResponse sdResponse) {
                LogUtil.d(actModel.toString());
                List<GoodsTypeModel> actModelTypes = actModel.getTypes();
                for (GoodsTypeModel actModelType : actModelTypes) {
                    int typeId = actModelType.getTypeId();
                    switch (typeId) {
                        case 1:
                            taskGoods = actModelType.getGoods();
                            break;
                        case 2:
                            lifeGoods = actModelType.getGoods();
                            break;
                    }
                }
                if (mAdapter != null) {
                    switch (radio_group_props.getCheckedRadioButtonId()) {
                        case R.id.radio_btn_task_props:
                            mAdapter.setData(taskGoods);
                            break;
                        case R.id.radio_btn_life_props:
                            mAdapter.setData(lifeGoods);
                            break;
                    }
                    mAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        setShowAnimator(SDAnimationUtil.alphaIn(this));
        setHideAnimator(SDAnimationUtil.alphaOut(this));
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
        if (mAdapter == null) {
            return;
        }
        switch (i) {
            case R.id.radio_btn_task_props:
                mAdapter.setData(taskGoods);
                break;
            case R.id.radio_btn_life_props:
                mAdapter.setData(lifeGoods);
                break;
        }
        mAdapter.notifyDataSetChanged();
    }
}
