package com.fanwe.library.dialog;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.fanwe.library.R;
import com.fanwe.library.adapter.SDSimpleTextAdapter;
import com.fanwe.library.drawable.SDDrawable;
import com.fanwe.library.utils.SDViewUtil;

import java.util.Arrays;
import java.util.List;

/**
 * 带取消按钮的菜单选择窗口
 *
 * @author js02
 */
public class SDDialogMenu extends SDDialogBase
{

    public TextView tv_title;
    public TextView tv_cancel;
    private LinearLayout ll_content;
    public ListView lv_content;

    private SDDialogMenuListener mListener;

    public SDDialogMenu setmListener(SDDialogMenuListener mListener)
    {
        this.mListener = mListener;
        return this;
    }

    public SDDialogMenu(Activity activity)
    {
        super(activity);
        init();
    }

    private void init()
    {
        setContentView(R.layout.dialog_menu);
        tv_title = (TextView) findViewById(R.id.dialog_menu_tv_title);
        tv_cancel = (TextView) findViewById(R.id.dialog_menu_tv_cancel);
        ll_content = (LinearLayout) findViewById(R.id.dialog_menu_ll_content);
        lv_content = (ListView) findViewById(R.id.dialog_menu_lv_content);

        initViewStates();
    }

    private void initViewStates()
    {
        tv_cancel.setOnClickListener(this);
        setTextTitle(null);
        setTextColorCancel(config.getMainColor());
        initDrawable();
        initListView();
        paddings(SDViewUtil.dp2px(10));
    }

    private void initListView()
    {
        SDViewUtil.setBackgroundDrawable(ll_content, drawableManager.getLayerWhiteStrokeItemSingle(true));
        setDivierEnable(true);
    }

    private void initDrawable()
    {
        int corner = config.getCornerRadius();

        //标题背景
        Drawable titleDrawable = new SDDrawable().color(Color.WHITE).cornerTopLeft(corner).cornerTopRight(corner);
        SDViewUtil.setBackgroundDrawable(tv_title, titleDrawable);

        //取消按钮背景
        Drawable cancelNormal = new SDDrawable().color(Color.WHITE).cornerAll(corner);
        Drawable cancelPressed = new SDDrawable().color(config.getGrayPressColor()).cornerAll(corner);
        Drawable cancelDrawable = SDDrawable.getStateListDrawable(cancelNormal, null, null, cancelPressed);
        SDViewUtil.setBackgroundDrawable(tv_cancel, cancelDrawable);
    }

    public SDDialogMenu setDivierEnable(boolean enable)
    {
        if (enable)
        {
            lv_content.setDivider(new SDDrawable().color(config.getStrokeColor()));
            lv_content.setDividerHeight(config.getStrokeWidth());
        } else
        {
            lv_content.setDivider(null);
            lv_content.setDividerHeight(0);
        }
        return this;
    }

    public SDDialogMenu setAdapter(BaseAdapter adapter)
    {
        lv_content.setAdapter(adapter);
        lv_content.setOnItemClickListener(new OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                if (mListener != null)
                {
                    mListener.onItemClick(view, (int) id, SDDialogMenu.this);
                }
                dismissAfterClick();
            }
        });
        return this;
    }

    public SDDialogMenu setItems(String[] arrString)
    {
        if (arrString != null && arrString.length > 0)
        {
            List<String> listString = Arrays.asList(arrString);
            SDSimpleTextAdapter<String> adapter = new SDSimpleTextAdapter<String>(listString, getOwnerActivity());
            setAdapter(adapter);
        }
        return this;
    }

    // --------------------------color

    public SDDialogMenu setTextColorCancel(int colors)
    {
        tv_cancel.setTextColor(colors);
        return this;
    }

    // ------------------------text

    public SDDialogMenu setTextTitle(String text)
    {
        if (TextUtils.isEmpty(text))
        {
            SDViewUtil.hide(tv_title);
        } else
        {
            SDViewUtil.show(tv_title);
            tv_title.setText(text);
        }
        return this;
    }

    public SDDialogMenu setTextCancel(String text)
    {
        if (TextUtils.isEmpty(text))
        {
            SDViewUtil.hide(tv_cancel);
        } else
        {
            SDViewUtil.show(tv_cancel);
            tv_cancel.setText(text);
        }
        return this;
    }

    @Override
    public void onClick(View v)
    {
        if (v == tv_cancel)
        {
            clickCancel(v);
        }
    }

    private void clickCancel(View v)
    {
        if (mListener != null)
        {
            mListener.onCancelClick(v, SDDialogMenu.this);
        }
        dismissAfterClick();
    }

    @Override
    public void onDismiss(DialogInterface dialog)
    {
        if (mListener != null)
        {
            mListener.onDismiss(SDDialogMenu.this);
        }
    }

    public interface SDDialogMenuListener
    {
        void onCancelClick(View v, SDDialogMenu dialog);

        void onDismiss(SDDialogMenu dialog);

        void onItemClick(View v, int index, SDDialogMenu dialog);
    }

}
