package com.fanwe.live.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.live.R;
import com.fanwe.live.appview.CommentListView;
import com.fanwe.live.appview.ExpandTextView;
import com.fanwe.live.dao.UserModelDao;
import com.fanwe.live.dialog.CommentDialog;
import com.fanwe.live.event.EShowFamilyET;
import com.fanwe.live.model.MultipleItem;
import com.fanwe.live.model.UserModel;
import com.fanwe.live.model.item.CommentConfig;
import com.fanwe.live.model.item.FamilyCommentItem;
import com.fanwe.live.model.item.FamilyDataItem;
import com.fanwe.live.model.item.HeaderItem;
import com.sunday.eventbus.SDEventManager;

import java.util.List;

public class BoxBarAdapter extends BaseMultiItemQuickAdapter<MultipleItem, BaseViewHolder> {

    private Context mContext;
    private UserModel mUserModel;

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public BoxBarAdapter(Context context, List<MultipleItem> data) {
        super(data);
        mContext = context;
        mUserModel = UserModelDao.query();
        addItemType(MultipleItem.HEAD, R.layout.item_multiple_head);
        addItemType(MultipleItem.POPULAR_VIDEO, R.layout.item_popular_video);
        addItemType(MultipleItem.BOX_BAR, R.layout.item_box_bar);
    }

    @Override
    protected void convert(BaseViewHolder helper, MultipleItem item) {
        switch (helper.getItemViewType()) {
            case MultipleItem.HEAD://分组标题
                HeaderItem headerItem = (HeaderItem) item;
                helper.setImageResource(R.id.iv_header_icon, headerItem.getResId());
                helper.setText(R.id.tv_header_text, headerItem.getTitle());
                helper.setVisible(R.id.tv_more, headerItem.isMore());
                break;
            case MultipleItem.POPULAR_VIDEO://热门视频
                break;
            case MultipleItem.BOX_BAR://箱吧
                final FamilyDataItem familyItem = (FamilyDataItem) item;
                //头像
                ImageView headIv = helper.getView(R.id.headIv);
                SDViewBinder.setRoundImageView(mContext, headIv, ((FamilyDataItem) item).headImage, R.drawable.ic_default_head);
                //昵称
                helper.setText(R.id.nameTv, familyItem.name);
                //发表时间
                helper.setText(R.id.timeTv, familyItem.happenTime);
                //发表文字内容
                ExpandTextView expandView = helper.getView(R.id.contentTv);
                expandView.setText(familyItem.content);
                //如果是自己发表的内容, 显示删除按钮
                helper.setVisible(R.id.deleteBtn, familyItem.userId.equals(mUserModel.getUserId()));
                //评论
                final List<FamilyCommentItem> comments = familyItem.comments;
                helper.setText(R.id.tv_comment_number, comments == null ?
                        String.valueOf(0) : String.valueOf(comments.size()));
                //处理评论列表
                CommentListView commentList = helper.getView(R.id.commentList);
                if (comments != null && comments.size() > 0) {
                    commentList.setDatas(comments);
                    commentList.setVisibility(View.VISIBLE);
                    commentList.setOnItemClickListener(new CommentListView.OnItemClickListener() {
                        @Override
                        public void onItemClick(int pos) {
                            FamilyCommentItem commentItem = comments.get(pos);
                            if (mUserModel.getUserId().equals(commentItem.userId)) {//复制或者删除自己的评论
                                CommentDialog dialog = new CommentDialog(mContext, commentItem);
                                dialog.show();
                            } else {//回复别人的评论
                                CommentConfig config = new CommentConfig();
                                config.commentPosition = pos;
                                config.commentType = CommentConfig.Type.REPLY;
                                config.zoneId = familyItem.userId;
                                UserModel replayUser = new UserModel();
                                replayUser.setUserId(commentItem.userId);
                                replayUser.setNickName(commentItem.userName);
                                config.replyUser = replayUser;
                                SDEventManager.post(new EShowFamilyET(config));
                            }
                        }
                    });
                } else {
                    commentList.setVisibility(View.GONE);
                }
                break;
        }
    }
}
