package com.fanwe.live.appview;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fanwe.auction.appview.AuctionTradeMsgView;
import com.fanwe.hybrid.http.AppRequestCallback;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.fanwe.library.blocker.SDBlocker;
import com.fanwe.library.common.SDHandlerManager;
import com.fanwe.library.common.SDSelectManager;
import com.fanwe.library.dialog.SDDialogConfirm;
import com.fanwe.library.dialog.SDDialogCustom;
import com.fanwe.library.model.SDTaskRunnable;
import com.fanwe.library.utils.SDResourcesUtil;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.library.view.SDTabUnderline;
import com.fanwe.library.view.select.SDSelectViewManager;
import com.fanwe.live.IMHelper;
import com.fanwe.live.R;
import com.fanwe.live.adapter.LiveChatUserAdapter;
import com.fanwe.live.common.AppRuntimeWorker;
import com.fanwe.live.common.CommonInterface;
import com.fanwe.live.dao.UserModelDao;
import com.fanwe.live.event.EImOnNewMessages;
import com.fanwe.live.event.ERefreshMsgUnReaded;
import com.fanwe.live.model.App_BaseInfoActModel;
import com.fanwe.live.model.App_my_follow_ActModel;
import com.fanwe.live.model.ItemLiveChatListModel;
import com.fanwe.live.model.TotalConversationUnreadMessageModel;
import com.fanwe.live.model.UserModel;
import com.fanwe.live.model.custommsg.MsgModel;
import com.fanwe.live.model.custommsg.TIMMsgModel;
import com.tencent.TIMConversation;
import com.tencent.TIMConversationType;
import com.tencent.TIMManager;
import com.tencent.TIMMessage;

import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/7/16.
 */
public class LiveChatC2CView extends BaseAppView
{
    @ViewInject(R.id.ll_back)
    private LinearLayout ll_back;

    @ViewInject(R.id.rl_auction_trade)
    private RelativeLayout rl_auction_trade;
    @ViewInject(R.id.tab_trade)
    private SDTabUnderline tab_trade;
    @ViewInject(R.id.ll_trade)
    private LinearLayout ll_trade;

    @ViewInject(R.id.tab_left)
    private SDTabUnderline tab_left;
    @ViewInject(R.id.tv_left_total)
    private TextView tv_left_total;
    @ViewInject(R.id.list_left)
    private ListView list_left;

    @ViewInject(R.id.list_right)
    private ListView list_right;
    @ViewInject(R.id.tab_right)
    private SDTabUnderline tab_right;
    @ViewInject(R.id.tv_right_total)
    private TextView tv_right_total;

    @ViewInject(R.id.ll_read)
    private LinearLayout ll_read;

    private AuctionTradeMsgView msg;//交易View

    private SDBlocker blocker = new SDBlocker(2000);

    private SDSelectViewManager<SDTabUnderline> mSelectManager = new SDSelectViewManager<SDTabUnderline>();

    private int mSelectTabIndex = 0;

    private List<UserModel> focusListModel = new ArrayList<UserModel>();

    private List<ItemLiveChatListModel> localChatListModel = new ArrayList<ItemLiveChatListModel>();

    private ArrayList<ItemLiveChatListModel> leftlistModel = new ArrayList<ItemLiveChatListModel>();
    private ArrayList<ItemLiveChatListModel> rightlistModel = new ArrayList<ItemLiveChatListModel>();

    private UserModel localuser = UserModelDao.query();

    private LiveChatUserAdapter adapterLeft;
    private LiveChatUserAdapter adapterRight;

    public LiveChatC2CView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        init();
    }

    public LiveChatC2CView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    public LiveChatC2CView(Context context)
    {
        super(context);
        init();
    }

    @Override
    protected void init()
    {
        super.init();
        setContentView(R.layout.view_live_chat_c2c);
        regsiter();
        addTradeMsgView();
        addTab();
        bindAdapterLeft();
        bindAdapterRight();
    }

    public void initDataThread()
    {
        initLocalC2CConver();
        requestMyFollow();
        msg.refreshViewer();
    }

    private void regsiter()
    {
        ll_back.setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                clickListener.onClickBack();
            }
        });

        ll_read.setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                if (blocker.block())
                {
                    return;
                }
                getC2CTotalUnreadMessageNum();
            }
        });
    }

    private void getC2CTotalUnreadMessageNum()
    {
        String user_id = localuser.getUserId();
        if (!TextUtils.isEmpty(user_id))
        {
            TotalConversationUnreadMessageModel model = IMHelper.getC2CTotalUnreadMessageModel();
            if (model.getTotalUnreadNum() > 0)
            {
                showDeleteUnReadMsgDialog();
            }
        }
    }

    private void addTradeMsgView()
    {
        msg = new AuctionTradeMsgView(getActivity());
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        ll_trade.addView(msg, lp);
    }

    private void addTab()
    {
        if (AppRuntimeWorker.getShow_hide_pai_view() == 1)
        {
            SDViewUtil.show(ll_trade);
            SDViewUtil.show(rl_auction_trade);
        } else
        {
            mSelectTabIndex = 1;
            SDViewUtil.hide(rl_auction_trade);
            SDViewUtil.hide(ll_trade);
        }

        tab_trade.setTextTitle(SDResourcesUtil.getString(R.string.trade));
        tab_trade.getViewConfig(tab_trade.mIvUnderline).setBackgroundColorNormal(Color.TRANSPARENT).setBackgroundColorSelected(SDResourcesUtil.getColor(R.color.white));
        tab_trade.getViewConfig(tab_trade.mIvUnderline).setTextColorNormal(SDResourcesUtil.getColor(R.color.white)).setTextColorSelected(SDResourcesUtil.getColor(R.color.white));
//        tab_trade.getViewConfig(tab_trade.mTvTitle).setTextColorNormalResId(R.color.text_title_bar).setTextColorSelectedResId(R.color.main_color);


        tab_left.setTextTitle(SDResourcesUtil.getString(R.string.good_friends));
        tab_left.getViewConfig(tab_left.mIvUnderline).setBackgroundColorNormal(Color.TRANSPARENT).setBackgroundColorSelected(SDResourcesUtil.getColor(R.color.white));
        tab_left.getViewConfig(tab_left.mTvTitle).setTextColorNormal(SDResourcesUtil.getColor(R.color.white)).setTextColorSelected(SDResourcesUtil.getColor(R.color.white));

        tab_right.setTextTitle(SDResourcesUtil.getString(R.string.not_follow));
        tab_right.getViewConfig(tab_right.mIvUnderline).setBackgroundColorNormal(Color.TRANSPARENT).setBackgroundColorSelected(SDResourcesUtil.getColor(R.color.white));
        tab_right.getViewConfig(tab_right.mTvTitle).setTextColorNormal(SDResourcesUtil.getColor(R.color.white)).setTextColorSelected(SDResourcesUtil.getColor(R.color.white));

        mSelectManager.setListener(new SDSelectManager.SDSelectManagerListener<SDTabUnderline>()
        {

            @Override
            public void onNormal(int index, SDTabUnderline item)
            {
            }

            @Override
            public void onSelected(int index, SDTabUnderline item)
            {
                switch (index)
                {
                    case 0:
                        SDViewUtil.show(ll_trade);
                        SDViewUtil.hide(list_left);
                        SDViewUtil.hide(list_right);
                        break;
                    case 1:
                        SDViewUtil.show(list_left);
                        SDViewUtil.hide(list_right);
                        SDViewUtil.hide(ll_trade);
                        break;
                    case 2:
                        SDViewUtil.show(list_right);
                        SDViewUtil.hide(list_left);
                        SDViewUtil.hide(ll_trade);
                        break;
                    default:
                        break;
                }
            }
        });

        mSelectManager.setItems(new SDTabUnderline[]
                {tab_trade, tab_left, tab_right});
        mSelectManager.performClick(mSelectTabIndex);
    }

    private void bindAdapterLeft()
    {
        adapterLeft = new LiveChatUserAdapter(leftlistModel, getActivity());
        list_left.setAdapter(adapterLeft);
        list_left.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                if (onVItemClickListener != null)
                {
                    ItemLiveChatListModel item = adapterLeft.getItem((int) id);
                    onVItemClickListener.onVItemClickListener(item, id);
                }
            }
        });
    }

    private void bindAdapterRight()
    {
        adapterRight = new LiveChatUserAdapter(rightlistModel, getActivity());
        list_right.setAdapter(adapterRight);
        list_right.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                if (onVItemClickListener != null)
                {
                    ItemLiveChatListModel item = adapterRight.getItem((int) id);
                    onVItemClickListener.onVItemClickListener(item, id);
                }
            }
        });
    }

    protected void onSuccessRequest()
    {
        if (focusListModel.size() > 0 && localChatListModel.size() > 0)
        {
            for (ItemLiveChatListModel itemChatListModel : localChatListModel)
            {
                if (focusListModel.contains(itemChatListModel))
                {
                    leftlistModel.add(itemChatListModel);
                } else
                {
                    rightlistModel.add(itemChatListModel);
                }
            }
        } else if (localChatListModel.size() > 0 && focusListModel.size() == 0)
        {
            for (ItemLiveChatListModel itemChatListModel : localChatListModel)
            {
                rightlistModel.add(itemChatListModel);
            }
        }

        if (leftlistModel.size() > 0)
        {
            requestBaseInfo(leftlistModel, adapterLeft);
        }

        if (rightlistModel.size() > 0)
        {
            requestBaseInfo(rightlistModel, adapterRight);
        }

        setUnReadNum();
    }

    private void setUnReadNum()
    {
        long total_num_left = 0;
        for (ItemLiveChatListModel item : leftlistModel)
        {
            long num = IMHelper.getConversationC2C(item.getPeer()).getUnreadMessageNum();
            total_num_left = num + total_num_left;
        }
        if (total_num_left > 0)
        {
            SDViewUtil.show(tv_left_total);
        } else
        {
            SDViewUtil.hide(tv_left_total);
        }
        tv_left_total.setText(Long.toString(total_num_left));

        long right_total_num = 0;
        for (ItemLiveChatListModel item : rightlistModel)
        {
            long num = IMHelper.getConversationC2C(item.getPeer()).getUnreadMessageNum();
            right_total_num = num + right_total_num;
        }
        if (right_total_num > 0)
        {
            SDViewUtil.show(tv_right_total);
        } else
        {
            SDViewUtil.hide(tv_right_total);
        }
        tv_right_total.setText(Long.toString(right_total_num));
    }

    private void initLocalC2CConver()
    {
        long cnt = TIMManager.getInstance().getConversationCount();
        for (long i = 0; i < cnt; ++i)
        {
            TIMConversation conversation = TIMManager.getInstance().getConversationByIndex(i);
            TIMConversationType type = conversation.getType();
            if (type == TIMConversationType.C2C)
            {
                // 自己对自己发的消息过滤
                if (!conversation.getPeer().equals(localuser.getUserId()))
                {
                    List<TIMMessage> list = conversation.getLastMsgs(1);
                    if (list != null && list.size() > 0)
                    {
                        TIMMessage msg = list.get(0);
                        MsgModel msgModel = new TIMMsgModel(msg);
                        if (msgModel.isPrivateMsg())
                        {
                            addListChatItem(msgModel);
                        }
                    }
                }
            }
        }
    }

    private void requestMyFollow()
    {
        CommonInterface.requestMyFollow(new AppRequestCallback<App_my_follow_ActModel>()
        {

            @Override
            protected void onSuccess(SDResponse resp)
            {
                if (actModel.getStatus() == 1)
                {
                    focusListModel = actModel.getList();
                }
            }

            @Override
            protected void onFinish(SDResponse resp)
            {
                super.onFinish(resp);
                onSuccessRequest();
            }

        });
    }

    private void addListChatItem(MsgModel msgModel)
    {
        ItemLiveChatListModel itemChatListModel = new ItemLiveChatListModel();
        itemChatListModel.setAllParams(msgModel);
        localChatListModel.add(itemChatListModel);
    }

    private void showDeleteUnReadMsgDialog()
    {
        SDDialogConfirm dialog = new SDDialogConfirm(getActivity());
        dialog.setTextContent(SDResourcesUtil.getString(R.string.if_ignore_all_not_read_msg));
        dialog.setTextCancel(SDResourcesUtil.getString(R.string.no));
        dialog.setTextConfirm(SDResourcesUtil.getString(R.string.yes));
        dialog.setmListener(new SDDialogCustom.SDDialogCustomListener()
        {

            @Override
            public void onDismiss(SDDialogCustom dialog)
            {
            }

            @Override
            public void onClickConfirm(View v, SDDialogCustom dialog)
            {
                setAllMsgReaded();
            }

            @Override
            public void onClickCancel(View v, SDDialogCustom dialog)
            {

            }
        });
        dialog.show();
    }

    private void setAllMsgReaded()
    {
        SDHandlerManager.getBackgroundHandler().post(new SDTaskRunnable<String>()
        {
            @Override
            public String onBackground()
            {
                return null;
            }

            @Override
            public void onMainThread(String result)
            {
                setUnReadNum();
            }
        });
        IMHelper.setAllC2CReadMessage();
        adapterLeft.notifyDataSetInvalidated();
        adapterRight.notifyDataSetInvalidated();
    }

    public void onEventMainThread(ERefreshMsgUnReaded event)
    {
        if (event.isFromSetLocalReaded)
        {
            SDHandlerManager.getBackgroundHandler().post(new SDTaskRunnable<String>()
            {
                @Override
                public String onBackground()
                {
                    return null;
                }

                @Override
                public void onMainThread(String result)
                {
                    setUnReadNum();
                }
            });
            adapterRight.notifyDataSetInvalidated();
            adapterLeft.notifyDataSetInvalidated();
        }
    }

    public void onEventMainThread(EImOnNewMessages event)
    {
        if (event.msg.isLocalPost())
        {

        } else
        {
            if (event.msg.isPrivateMsg())
            {
                dealNewC2CMsg(event.msg);
            }
        }
    }

    private void dealNewC2CMsg(MsgModel msg)
    {
        UserModel user = msg.getCustomMsg().getSender();
        updateList(user, msg);
    }

    private void updateList(UserModel user, MsgModel msg)
    {
        boolean isBelongLeftList = false;
        for (int i = 0; i < leftlistModel.size(); ++i)
        {
            ItemLiveChatListModel itemLiveChatListModel = leftlistModel.get(i);
            if (itemLiveChatListModel.equals(user))
            {
                isBelongLeftList = true;
                itemLiveChatListModel.setAllParams(msg);
                adapterLeft.sortListModel(i, itemLiveChatListModel);
                break;
            }
        }

        if (!isBelongLeftList)
        {
            boolean isBelongRightList = false;
            for (int i = 0; i < rightlistModel.size(); ++i)
            {
                ItemLiveChatListModel itemLiveChatListModel = rightlistModel.get(i);
                if (itemLiveChatListModel.equals(user))
                {
                    isBelongRightList = true;
                    itemLiveChatListModel.setAllParams(msg);
                    adapterRight.sortListModel(i, itemLiveChatListModel);
                    break;
                }
            }

            if (!isBelongRightList)
            {
                boolean isAddLeftList = false;
                for (int i = 0; i < focusListModel.size(); ++i)
                {
                    UserModel user_focus = focusListModel.get(i);
                    if (user_focus.equals(user))
                    {
                        isAddLeftList = true;
                        ItemLiveChatListModel itemLiveChatListModel = new ItemLiveChatListModel();
                        itemLiveChatListModel.setAllParams(msg);
                        adapterLeft.insertData(0, itemLiveChatListModel);
                        localChatListModel.add(itemLiveChatListModel);
                        break;
                    }
                }

                if (!isAddLeftList)
                {
                    ItemLiveChatListModel itemLiveChatListModel = new ItemLiveChatListModel();
                    itemLiveChatListModel.setAllParams(msg);
                    adapterRight.insertData(0, itemLiveChatListModel);
                    localChatListModel.add(itemLiveChatListModel);
                }
            }
        }

        SDHandlerManager.getBackgroundHandler().post(new SDTaskRunnable<String>()
        {
            @Override
            public String onBackground()
            {
                return null;
            }

            @Override
            public void onMainThread(String result)
            {
                setUnReadNum();
            }
        });
    }

    private ClickListener clickListener;

    public interface ClickListener
    {
        void onClickBack();
    }

    public void setClickListener(ClickListener clickListener)
    {
        this.clickListener = clickListener;
    }

    public OnVItemClickListener onVItemClickListener;

    public void setOnVItemClickListener(OnVItemClickListener onVItemClickListener)
    {
        this.onVItemClickListener = onVItemClickListener;
    }

    public interface OnVItemClickListener
    {
        void onVItemClickListener(ItemLiveChatListModel itemLiveChatListModel, long id);
    }

    private void requestBaseInfo(final List<ItemLiveChatListModel> r_list, final LiveChatUserAdapter adapt)
    {
        if (r_list != null && r_list.size() > 0)
        {
            StringBuilder sb = new StringBuilder();
            for (ItemLiveChatListModel itemLiveChatListModel : r_list)
            {
                String user_id = itemLiveChatListModel.getUserId();
                sb.append(user_id).append(",");
            }
            String ids = sb.toString();
            ids = ids.substring(0, ids.length() - 1);

            CommonInterface.requestBaseInfo(ids, new AppRequestCallback<App_BaseInfoActModel>()
            {
                @Override
                protected void onSuccess(SDResponse resp)
                {
                    if (actModel.getStatus() == 1)
                    {
                        List<UserModel> list = actModel.getList();
                        if (list != null && list.size() > 0 && list.size() == r_list.size())
                        {
                            for (int i = 0; i < list.size(); i++)
                            {
                                UserModel user = list.get(i);

                                ItemLiveChatListModel item = r_list.get(i);
                                item.setHeadImage(user.getHeadImage());
                                item.setNickName(user.getNickName());
                                item.setSex(user.getSex());
                                item.setUserLevel(user.getUserLevel());
                            }
                            adapt.notifyDataSetChanged();
                        }
                    }
                }
            });
        }
    }
}
