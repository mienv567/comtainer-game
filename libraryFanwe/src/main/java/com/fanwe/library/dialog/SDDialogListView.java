package com.fanwe.library.dialog;

import android.app.Activity;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.fanwe.library.R;

/**
 * 带标题，listview，确定按钮和取消按钮的窗口
 *
 * @author js02
 */
public class SDDialogListView extends SDDialogCustom
{

    public ListView lv_content;

    public SDDialogListView(Activity activity)
    {
        super(activity);
    }

    @Override
    protected void init()
    {
        super.init();

        setCustomView(R.layout.dialog_listview);
        lv_content = (ListView) findViewById(R.id.lv_content);
    }

    public void setAdapter(BaseAdapter adapter)
    {
        lv_content.setAdapter(adapter);
    }
}
