package com.fanwe.live.model;

import android.view.View;

import com.fanwe.library.common.SDSelectManager.SDSelectable;

import java.lang.ref.WeakReference;

public class LiveGiftModel implements SDSelectable {

    private int propId;
    private String name; // 礼物名字
    private int score; // 积分
    private int diamonds; // 钻石
    private String icon; // 图标
    private String animatedUrl; // 动画地址
    private int ticket; // 钱票
    private int is_much; // 1-可以连续发送多个，用于小金额礼物
    private int sort;
    private int isRedEnvelope;// 1-红包
    private String scoreFromat; //主播增加的经验

    // add
    private boolean selected;
    private int superimposedType; //#新增#-叠加类型:0=没有数量,1=对每个用户有数量
    private int propCount; //#新增#-礼物数量(要先判断superimposed_type
    private String tips;// 有数量的礼物，当数量为0的时候 ，点击发送需要提示的内容
    private String previewUrl;//#增加#-礼物预览gif图
    private WeakReference<View> hodler;
    /**
     * isRedEnvelope : 0
     * isAnimated : 0
     * propId : 1
     * animType :
     * isMuch : 1
     * isEffect : 1
     */

    private int isAnimated;
    private String animType;
    private int isMuch;
    private int isEffect;


    public String getScoreFromat() {
        return scoreFromat;
    }

    public void setScoreFromat(String scoreFromat) {
        this.scoreFromat = scoreFromat;
    }

    public int getPropId() {
        return propId;
    }

    public void setPropId(int propId) {
        this.propId = propId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getDiamonds() {
        return diamonds;
    }

    public void setDiamonds(int diamonds) {
        this.diamonds = diamonds;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getTicket() {
        return ticket;
    }

    public void setTicket(int ticket) {
        this.ticket = ticket;
    }

    public int getIs_much() {
        return is_much;
    }

    public void setIs_much(int is_much) {
        this.is_much = is_much;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    @Override
    public boolean isSelected() {
        return selected;
    }

    @Override
    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public int getIsRedEnvelope() {
        return isRedEnvelope;
    }

    public void setIsRedEnvelope(int isRedEnvelope) {
        this.isRedEnvelope = isRedEnvelope;
    }

    public String getAnimatedUrl() {
        return animatedUrl;
    }

    public void setAnimatedUrl(String animatedUrl) {
        this.animatedUrl = animatedUrl;
    }

    public int getPropCount() {
        return propCount;
    }

    public void setPropCount(int propCount) {
        this.propCount = propCount;
    }

    public int getSuperimposedType() {
        return superimposedType;
    }

    public void setSuperimposedType(int superimposedType) {
        this.superimposedType = superimposedType;
    }

    public WeakReference<View> getHodler() {
        return hodler;
    }

    public void setHodler(WeakReference<View> hodler) {
        this.hodler = hodler;
    }

    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }

    public String getPreviewUrl() {
        return previewUrl;
    }

    public void setPreviewUrl(String previewUrl) {
        this.previewUrl = previewUrl;
    }


    public int getIsAnimated() {
        return isAnimated;
    }

    public void setIsAnimated(int isAnimated) {
        this.isAnimated = isAnimated;
    }


    public String getAnimType() {
        return animType;
    }

    public void setAnimType(String animType) {
        this.animType = animType;
    }

    public int getIsMuch() {
        return isMuch;
    }

    public void setIsMuch(int isMuch) {
        this.isMuch = isMuch;
    }

    public int getIsEffect() {
        return isEffect;
    }

    public void setIsEffect(int isEffect) {
        this.isEffect = isEffect;
    }
}
