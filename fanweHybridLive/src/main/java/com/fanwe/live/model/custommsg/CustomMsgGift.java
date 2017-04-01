package com.fanwe.live.model.custommsg;

import com.fanwe.live.LiveConstant;
import com.fanwe.live.LiveConstant.CustomMsgType;
import com.fanwe.live.gif.GifConfigModel;
import com.fanwe.live.model.UserModel;

import java.util.List;

public class CustomMsgGift extends CustomMsg implements ILiveGiftMsg
{
    private int prop_id; // 礼物id
    private String animated_url; // 动画播放url
    private String icon; // 图片
    private int num; // 礼物数量
    private int is_plus; // 是否需要叠加数量，1-是
    private int is_much; // 是否可以连发
    private int is_animated = LiveConstant.GiftType.NORMAL; // 动画类型
    private String anim_type; // 动画类型，当is_animated=2时候有效
    private long totalTicket; // 总钱票数量
    private String toUserId; // 礼物接收人(主播)
    private String fonts_color; // 字体颜色
    private String desc; // 观众收到的提示内容
    private String desc2; // 主播收到的提示内容
    private String desc3; // 弹出礼物 显示的文字
    private String top_title;
    private String preview_url;//#增加#-礼物预览gif图
    private long totalActivityTicket; // 战斗力值

    public long getTotalActivityTicket() {
        return totalActivityTicket;
    }

    public void setTotalActivityTicket(long totalActivityTicket) {
        this.totalActivityTicket = totalActivityTicket;
    }
    private List<GifConfigModel> anim_cfg;

    // add
    private int showNum = 1;
    private boolean isTaked = false;

    public CustomMsgGift()
    {
        super();
        setType(CustomMsgType.MSG_GIFT);
    }

    public String getTop_title()
    {
        return top_title;
    }

    public void setTop_title(String top_title)
    {
        this.top_title = top_title;
    }

    public String getAnim_type()
    {
        return anim_type;
    }

    public void setAnim_type(String anim_type)
    {
        this.anim_type = anim_type;
    }

    @Override
    public int getShowNum()
    {
        return showNum;
    }

    @Override
    public UserModel getMsgSender()
    {
        return getSender();
    }

    @Override
    public void setShowNum(int showNum)
    {
        this.showNum = showNum;
    }

    //add
    public boolean isGif()
    {
        return is_animated == LiveConstant.GiftType.GIF;
    }

    public boolean isAnimatior()
    {
        return is_animated == LiveConstant.GiftType.ANIMATOR;
    }

    // add
    public String getHashKey()
    {
        StringBuilder sb = new StringBuilder();
        UserModel user = getSender();
        if (user != null)
        {
            sb.append(user.getUserId());
        }
        sb.append(String.valueOf(prop_id));

        return sb.toString();
    }

    public int getProp_id()
    {
        return prop_id;
    }

    public void setProp_id(int prop_id)
    {
        this.prop_id = prop_id;
    }

    public String getAnimated_url()
    {
        return animated_url;
    }

    public void setAnimated_url(String animated_url)
    {
        this.animated_url = animated_url;
    }

    public String getIcon()
    {
        return icon;
    }

    public void setIcon(String icon)
    {
        this.icon = icon;
    }

    public long getTotalTicket()
    {
        return totalTicket;
    }

    public void setTotalTicket(long totalTicket)
    {
        this.totalTicket = totalTicket;
    }

    public String getToUserId()
    {
        return toUserId;
    }

    public void setToUserId(String toUserId)
    {
        this.toUserId = toUserId;
    }

    public String getFonts_color()
    {
        return fonts_color;
    }

    public void setFonts_color(String fonts_color)
    {
        this.fonts_color = fonts_color;
    }

    public String getDesc()
    {
        return desc;
    }

    public void setDesc(String desc)
    {
        this.desc = desc;
    }

    public String getDesc2()
    {
        return desc2;
    }

    public void setDesc2(String desc2)
    {
        this.desc2 = desc2;
    }

    @Override
    public int hashCode()
    {
        String userId = null;
        String giftId = String.valueOf(prop_id);
        if (getSender() != null)
        {
            userId = getSender().getUserId();
        }

        return (userId + giftId).hashCode();
    }

    @Override
    public boolean equals(Object o)
    {
        if (o == null)
        {
            return false;
        }

        if (!(o instanceof CustomMsgGift))
        {
            return false;
        }

        CustomMsgGift msg = (CustomMsgGift) o;

        if (prop_id <= 0)
        {
            return false;
        }

        if (prop_id != msg.getProp_id())
        {
            return false;
        }

        if (getSender() == null)
        {
            return false;
        }

        if (!getSender().equals(msg.getSender()))
        {
            return false;
        }

        return true;
    }

    public int getNum()
    {
        return num;
    }

    public void setNum(int num)
    {
        this.num = num;
    }

    public int getIs_plus()
    {
        return is_plus;
    }

    public void setIs_plus(int is_plus)
    {
        this.is_plus = is_plus;
    }

    @Override
    public boolean isTaked()
    {
        return isTaked;
    }

    @Override
    public void setTaked(boolean isTaked)
    {
        this.isTaked = isTaked;
    }

    @Override
    public boolean canPlay()
    {
        return !isGif() && !isAnimatior();
    }

    @Override
    public boolean needPlus()
    {
        return is_plus == 1;
    }

    @Override
    public boolean isPlusMode()
    {
        return is_much == 1;
    }

    public int getIs_much()
    {
        return is_much;
    }

    public void setIs_much(int is_much)
    {
        this.is_much = is_much;
    }

    public int getIs_animated()
    {
        return is_animated;
    }

    public void setIs_animated(int is_animated)
    {
        this.is_animated = is_animated;
    }

    public List<GifConfigModel> getAnim_cfg()
    {
        return anim_cfg;
    }

    public void setAnim_cfg(List<GifConfigModel> anim_cfg)
    {
        this.anim_cfg = anim_cfg;
    }

    public String getPreview_url() {
        return preview_url;
    }

    public void setPreview_url(String preview_url) {
        this.preview_url = preview_url;
    }

    public String getDesc3() {
        return desc3;
    }

    public void setDesc3(String desc3) {
        this.desc3 = desc3;
    }
}
