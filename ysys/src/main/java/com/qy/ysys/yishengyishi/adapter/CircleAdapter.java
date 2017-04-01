package com.qy.ysys.yishengyishi.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.qy.ysys.yishengyishi.AppImpl;
import com.qy.ysys.yishengyishi.BaseRecycleViewAdapter;
import com.qy.ysys.yishengyishi.R;
import com.qy.ysys.yishengyishi.adapter.viewholder.CircleViewHolder;
import com.qy.ysys.yishengyishi.adapter.viewholder.HeadHotTopicHolder;
import com.qy.ysys.yishengyishi.adapter.viewholder.ImageViewHolder;
import com.qy.ysys.yishengyishi.adapter.viewholder.URLViewHolder;
import com.qy.ysys.yishengyishi.event.EAddFamilyPraise;
import com.qy.ysys.yishengyishi.event.EDeleteFamily;
import com.qy.ysys.yishengyishi.event.EDeleteFamilyPraise;
import com.qy.ysys.yishengyishi.event.EShowFamilyET;
import com.qy.ysys.yishengyishi.model.CommentConfig;
import com.qy.ysys.yishengyishi.model.item.ActionItem;
import com.qy.ysys.yishengyishi.model.item.FamilyCommentItem;
import com.qy.ysys.yishengyishi.model.item.FamilyDataItem;
import com.qy.ysys.yishengyishi.model.item.FamilyPraiseItem;
import com.qy.ysys.yishengyishi.model.item.UserInfo;
import com.qy.ysys.yishengyishi.utils.GlideCircleTransform;
import com.qy.ysys.yishengyishi.utils.UrlUtils;
import com.qy.ysys.yishengyishi.views.ImagePagerActivity;
import com.qy.ysys.yishengyishi.widgets.CommentListView;
import com.qy.ysys.yishengyishi.widgets.ExpandTextView;
import com.qy.ysys.yishengyishi.widgets.MultiImageView;
import com.qy.ysys.yishengyishi.widgets.PraiseListView;
import com.qy.ysys.yishengyishi.widgets.SnsPopupWindow;
import com.qy.ysys.yishengyishi.widgets.dialog.CommentDialog;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by yiwei on 16/5/17.
 */
public class CircleAdapter extends BaseRecycleViewAdapter {

    private static final int STATE_IDLE = 0;
    private static final int STATE_ACTIVED = 1;
    private static final int STATE_DEACTIVED = 2;
    private int videoState = STATE_IDLE;
    public static final int HEADVIEW_SIZE = 1;
    private UserInfo mUserModel;
    int curPlayIndex = -1;

    private Context context;

    private List headerHotTopicData = new ArrayList();

    public CircleAdapter(Context context) {
        this.context = context;
        initUser();
    }

    private void initUser() {
        mUserModel = AppImpl.getCurrentUser();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return CircleViewHolder.TYPE_HEAD;
        }
        int itemType = 0;
//        FamilyDataItem item = (FamilyDataItem) datas.get();
//        if (CircleItem.TYPE_URL.equals(item.getType())) {
//            itemType = CircleViewHolder.TYPE_URL;
//        } else if (CircleItem.TYPE_IMG.equals(item.getType())) {
//            itemType = CircleViewHolder.TYPE_IMAGE;
//        } else if(CircleItem.TYPE_VIDEO.equals(item.getType())){
//            itemType = CircleViewHolder.TYPE_VIDEO;
//        }
        itemType = CircleViewHolder.TYPE_IMAGE;
        return itemType;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
//        if(viewType == TYPE_HEAD){
//            View headView = LayoutInflater.from(parent.getContext()).inflate(R.layout.head_circle, parent, false);
//            viewHolder = new HeaderViewHolder(headView);
//        }else{
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_circle_item, parent, false);

        if (viewType == CircleViewHolder.TYPE_URL) {
            viewHolder = new URLViewHolder(view);
        } else if (viewType == CircleViewHolder.TYPE_IMAGE) {
            viewHolder = new ImageViewHolder(view);
        } else if (viewType == CircleViewHolder.TYPE_HEAD) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_head_hot_topic, parent, false);
            viewHolder = new HeadHotTopicHolder(view);
        }
//            else if(viewType == CircleViewHolder.TYPE_VIDEO){
//                viewHolder = new VideoViewHolder(view);
//            }
//        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
        int itemViewType = getItemViewType(position);
        // 如果是最上端的热门视频
        if (itemViewType == CircleViewHolder.TYPE_HEAD) {
            HeadHotTopicHolder holder = (HeadHotTopicHolder) viewHolder;
            holder.setData(headerHotTopicData);
            return;
        }
        final int circlePosition = position - 1;
        final CircleViewHolder holder = (CircleViewHolder) viewHolder;
        final FamilyDataItem circleItem = (FamilyDataItem) datas.get(circlePosition);
        final int circleId = circleItem.getId();
        String name = circleItem.getName();
        String headImg = circleItem.getHeadImage();
        String time = circleItem.getHappenTime();
        final String content = circleItem.getContent();
        String createTime = circleItem.getCreateTime();
        final List<FamilyPraiseItem> favortDatas = circleItem.getPraises();
        final List<FamilyCommentItem> commentsDatas = circleItem.getComments();
        boolean hasFavort = circleItem.hasFavort();
        boolean hasComment = circleItem.hasComment();

        Glide.with(context).load(headImg).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.color.bg_no_photo).transform(new GlideCircleTransform(context)).into(holder.headIv);

        holder.nameTv.setText(name);
        holder.timeTv.setText(createTime);
        holder.tv_from_who.setText(time);

        if (!TextUtils.isEmpty(content)) {
            holder.contentTv.setExpand(/*circleItem.isExpand()*/true);
            holder.contentTv.setExpandStatusListener(new ExpandTextView.ExpandStatusListener() {
                @Override
                public void statusChange(boolean isExpand) {
                    circleItem.setIsExpand(isExpand);
                }
            });

            holder.contentTv.setText(UrlUtils.formatUrlString(content));
        }
        holder.contentTv.setVisibility(TextUtils.isEmpty(content) ? View.GONE : View.VISIBLE);

        if (circleItem.getUserId() == AppImpl.getCurrentUser().getId()) {
            holder.deleteBtn.setVisibility(View.VISIBLE);
        } else {
            holder.deleteBtn.setVisibility(View.GONE);
        }
        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //删除
                showDeleteDialog(circleId);
//                    if(presenter!=null){
//                        presenter.deleteCircle(circleId);
//                    }
            }
        });
        if (hasFavort || hasComment) {
            if (hasFavort) {//处理点赞列表
                holder.praiseListView.setOnItemClickListener(new PraiseListView.OnItemClickListener() {
                    @Override
                    public void onClick(int position) {
//                            String userName = favortDatas.get(position).getUser().getName();
//                            String userId = favortDatas.get(position).getUser().getId();
//                            Toast.makeText(MyApplication.getContext(), userName + " &id = " + userId, Toast.LENGTH_SHORT).show();
                    }
                });
                holder.praiseListView.setDatas(favortDatas);
                holder.praiseListView.setVisibility(View.VISIBLE);
            } else {
                holder.praiseListView.setVisibility(View.GONE);
            }

            if (hasComment) {//处理评论列表
                holder.commentList.setOnItemClickListener(new CommentListView.OnItemClickListener() {
                    @Override
                    public void onItemClick(int commentPosition) {
                        FamilyCommentItem commentItem = commentsDatas.get(commentPosition);
                        if (mUserModel.getId() == commentItem.getUserId()) {//复制或者删除自己的评论
                            CommentDialog dialog = new CommentDialog(context, commentItem, circlePosition);
                            dialog.show();
                        } else {//回复别人的评论
                            CommentConfig config = new CommentConfig();
                            config.circlePosition = circlePosition;
                            config.commentPosition = commentPosition;
                            config.commentType = CommentConfig.Type.REPLY;
                            config.zoneId = circleItem.getId();
                            UserInfo replayUser = new UserInfo();
                            replayUser.setId(commentItem.getUserId());
                            replayUser.setName(commentItem.getUserName());
                            config.replyUser = replayUser;
                            EventBus.getDefault().post(new EShowFamilyET(config));
                        }
                    }
                });
                holder.commentList.setOnItemLongClickListener(new CommentListView.OnItemLongClickListener() {
                    @Override
                    public void onItemLongClick(int commentPosition) {
                        //长按进行复制或者删除
                        FamilyCommentItem commentItem = commentsDatas.get(commentPosition);
                        CommentDialog dialog = new CommentDialog(context, commentItem, circlePosition);
                        dialog.show();
                    }
                });
                holder.commentList.setDatas(commentsDatas);
                holder.commentList.setVisibility(View.VISIBLE);

            } else {
                holder.commentList.setVisibility(View.GONE);
            }
            holder.digCommentBody.setVisibility(View.VISIBLE);
        } else {
            holder.digCommentBody.setVisibility(View.GONE);
        }

        holder.digLine.setVisibility(hasFavort && hasComment ? View.VISIBLE : View.GONE);

        final SnsPopupWindow snsPopupWindow = holder.snsPopupWindow;
        //判断是否已点赞
        int curUserFavortId = circleItem.getCurUserFavortId(mUserModel.getId());
        if (curUserFavortId != -1) {
            snsPopupWindow.getmActionItems().get(0).mTitle = "取消";
        } else {
            snsPopupWindow.getmActionItems().get(0).mTitle = "赞";
        }
        snsPopupWindow.update();
        snsPopupWindow.setmItemClickListener(new PopupItemClickListener(circlePosition, circleItem, curUserFavortId));
        holder.snsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //弹出popupwindow
                snsPopupWindow.showPopupWindow(view);
            }
        });

        holder.tv_comment_number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.digCommentBody.getVisibility() == View.VISIBLE) {
                    holder.digCommentBody.setVisibility(View.GONE);
                } else {
                    if (holder.commentList.getDatas() != null && holder.commentList.getDatas().size() > 0) {
                        holder.digCommentBody.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        holder.urlTipTv.setVisibility(View.GONE);
        switch (holder.viewType) {
            case CircleViewHolder.TYPE_URL:// 处理链接动态的链接内容和和图片
                if (holder instanceof URLViewHolder) {
//                        String linkImg = circleItem.getLinkImg();
//                        String linkTitle = circleItem.getLinkTitle();
//                        Glide.with(context).load(linkImg).into(((URLViewHolder)holder).urlImageIv);
//                        ((URLViewHolder)holder).urlContentTv.setText(linkTitle);
//                        ((URLViewHolder)holder).urlBody.setVisibility(View.VISIBLE);
//                        ((URLViewHolder)holder).urlTipTv.setVisibility(View.VISIBLE);
                }

                break;
            case CircleViewHolder.TYPE_IMAGE:// 处理图片
                if (holder instanceof ImageViewHolder) {
                    final List<String> photoUrls = circleItem.getImages();
                    if (photoUrls != null && photoUrls.size() > 0) {
                        ((ImageViewHolder) holder).multiImageView.setVisibility(View.VISIBLE);
                        if (photoUrls.size() > 1) {
                            ((ImageViewHolder) holder).tv_pic_number.setVisibility(View.VISIBLE);
                            ((ImageViewHolder) holder).tv_pic_number.setText("共" + photoUrls.size() + "张");
                        } else {
                            ((ImageViewHolder) holder).tv_pic_number.setVisibility(View.GONE);
                        }
                        ArrayList<String> imgs = new ArrayList<>();
                        imgs.add(photoUrls.get(0));
                        ((ImageViewHolder) holder).multiImageView.setList(imgs);
//                        ((ImageViewHolder) holder).multiImageView.setList(photoUrls);
                        ((ImageViewHolder) holder).multiImageView.setOnItemClickListener(new MultiImageView.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                //imagesize是作为loading时的图片size
                                ImagePagerActivity.ImageSize imageSize = new ImagePagerActivity.ImageSize(view.getMeasuredWidth(), view.getMeasuredHeight());

                                ImagePagerActivity.startImagePagerActivity(context, photoUrls, position, imageSize);


                            }
                        });
                    } else {
                        ((ImageViewHolder) holder).multiImageView.setVisibility(View.GONE);
                        ((ImageViewHolder) holder).tv_pic_number.setVisibility(View.GONE);
                    }
                }

                break;
            case CircleViewHolder.TYPE_VIDEO:
//                    if(holder instanceof VideoViewHolder){
//                        ((VideoViewHolder)holder).videoView.setVideoUrl(circleItem.getVideoUrl());
//                        ((VideoViewHolder)holder).videoView.setVideoImgUrl(circleItem.getVideoImgUrl());//视频封面图片
//                        ((VideoViewHolder)holder).videoView.setPostion(position);
//                        ((VideoViewHolder)holder).videoView.setOnPlayClickListener(new CircleVideoView.OnPlayClickListener() {
//                            @Override
//                            public void onPlayClick(int pos) {
//                                curPlayIndex = pos;
//                            }
//                        });
//                    }

                break;
            default:
                break;
        }

    }

    @Override
    public int getItemCount() {
        return datas.size() + 1;//有head需要加1
    }

    private void showDeleteDialog(final int zoneId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("确定要删除吗？");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EventBus.getDefault().post(new EDeleteFamily(zoneId));
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    private class PopupItemClickListener implements SnsPopupWindow.OnItemClickListener {
        private int mFavorId;
        //动态在列表中的位置
        private int mCirclePosition;
        private long mLasttime = 0;
        private FamilyDataItem mCircleItem;

        public PopupItemClickListener(int circlePosition, FamilyDataItem circleItem, int favorId) {
            this.mFavorId = favorId;
            this.mCirclePosition = circlePosition;
            this.mCircleItem = circleItem;
        }

        @Override
        public void onItemClick(ActionItem actionitem, int position) {
            switch (position) {
                case 0://点赞、取消点赞
                    if (System.currentTimeMillis() - mLasttime < 700)//防止快速点击操作
                        return;
                    mLasttime = System.currentTimeMillis();
//                    if(presenter != null){
                    if ("赞".equals(actionitem.mTitle.toString())) {
                        EventBus.getDefault().post(new EAddFamilyPraise(mCirclePosition, mCircleItem.getId()));
//                            presenter.addFavort(mCirclePosition);
                    } else {//取消点赞
                        EventBus.getDefault().post(new EDeleteFamilyPraise(mCirclePosition, mFavorId));
//                                     presenter.deleteFavort(mCirclePosition, mFavorId);
                    }
//                    }
                    break;
                case 1://发布评论
//                    if(presenter != null){
                    CommentConfig config = new CommentConfig();
                    config.circlePosition = mCirclePosition;
                    config.commentType = CommentConfig.Type.PUBLIC;
                    config.zoneId = mCircleItem.getId();
                    EventBus.getDefault().post(new EShowFamilyET(config));
//                        presenter.showEditTextBody(config);
//                    }
                    break;
                default:
                    break;
            }
        }
    }
}
