package com.fanwe.live.activity;

import android.app.ListActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.fanwe.hybrid.app.App;
import com.fanwe.hybrid.constant.ApkConstant;
import com.fanwe.library.utils.SDToast;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Created by kevin.liu on 2017/3/2.
 */

public class LiveChooseServerActivity extends ListActivity {

    int lastPosition = -1;
    private ArrayList<String> list = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            Properties prop = new Properties();
            InputStream fs = this.getAssets().open("serverAddress.properties");
            prop.load(fs);
            int itemCount = Integer.valueOf(prop.getProperty("addressCount"));
            for (int i = 0; i < itemCount; i++) {
                list.add(prop.getProperty("server" + i));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);
        setListAdapter(adapter);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        SDToast.showToast(list.get(position));
        if (position == lastPosition) {
            saveAddress(position);
        }
        lastPosition = position;
    }

    private void saveAddress(int position) {
        getSharedPreferences(ApkConstant.SERVER_SP,MODE_PRIVATE).edit().putInt(ApkConstant.SERVER_SP_SELECT,position).apply();
        finish();
        App.getApplication().exitApp(false);
    }

}
