package com.fanwe.live.model.custommsg;

import com.fanwe.live.LiveConstant.CustomMsgType;
import com.fanwe.live.model.LRSUserModel;

import java.util.List;

public class CustomMsgLRS extends CustomMsg
{
    /**
     * 值为1 参与游戏提示 2 报名信息 3 游戏超时 4开始游戏 - 得到用户信息 以及 频道信息 5表示狼人和先知活动了
     * 6 女巫活动 7 狼人投票过程
     */
    private int step ;
    private int num; //当前参与游戏人数
    private int down_num;//游戏开始最低人数
    private int up_num;//游戏开始最多人数
    private int wait;//等待时间
    private int wait1;
    private int wait2;
    private int wait3;
    private int pass_wait;//发言选择连麦时间
    private List<LRSUserModel> members;//游戏成员
    private String game_group_id;//游戏频道id
    private String wolf_group_id;//狼人频道id
    private String user_id;//狼人投票过程 - 狼人id
    private String kill_user_id;//狼人投票过程 -被狼人点投票的玩家id
    private int poison;//女巫活动 - 1：有毒药; 0：没毒药
    private int antidote;//女巫活动 - 1：有解药; 0：没解药
    private String wolf_kill_user_id;//女巫活动 - 上一轮被狼人杀死的玩家id
    private List<String> dead;//玩家死亡名单
    private int win; //1代表狼人赢了；2代表好人赢了
    private String rule;//游戏规则
    public CustomMsgLRS()
    {
        super();
        setType(CustomMsgType.MSG_LRS);
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int getDown_num() {
        return down_num;
    }

    public void setDown_num(int down_num) {
        this.down_num = down_num;
    }

    public int getUp_num() {
        return up_num;
    }

    public void setUp_num(int up_num) {
        this.up_num = up_num;
    }

    public String getGame_group_id() {
        return game_group_id;
    }

    public void setGame_group_id(String game_group_id) {
        this.game_group_id = game_group_id;
    }

    public List<LRSUserModel> getMembers() {
        return members;
    }

    public void setMembers(List<LRSUserModel> members) {
        this.members = members;
    }

    public int getWait() {
        return wait;
    }

    public void setWait(int wait) {
        this.wait = wait;
    }

    public String getWolf_group_id() {
        return wolf_group_id;
    }

    public void setWolf_group_id(String wolf_group_id) {
        this.wolf_group_id = wolf_group_id;
    }

    public String getKill_user_id() {
        return kill_user_id;
    }

    public void setKill_user_id(String kill_user_id) {
        this.kill_user_id = kill_user_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public int getAntidote() {
        return antidote;
    }

    public void setAntidote(int antidote) {
        this.antidote = antidote;
    }

    public int getPoison() {
        return poison;
    }

    public void setPoison(int poison) {
        this.poison = poison;
    }

    public String getWolf_kill_user_id() {
        return wolf_kill_user_id;
    }

    public void setWolf_kill_user_id(String wolf_kill_user_id) {
        this.wolf_kill_user_id = wolf_kill_user_id;
    }

    public int getWait1() {
        return wait1;
    }

    public void setWait1(int wait1) {
        this.wait1 = wait1;
    }

    public int getWait2() {
        return wait2;
    }

    public void setWait2(int wait2) {
        this.wait2 = wait2;
    }

    public int getWait3() {
        return wait3;
    }

    public void setWait3(int wait3) {
        this.wait3 = wait3;
    }

    public List<String> getDead() {
        return dead;
    }

    public void setDead(List<String> dead) {
        this.dead = dead;
    }

    public int getPass_wait() {
        return pass_wait;
    }

    public void setPass_wait(int pass_wait) {
        this.pass_wait = pass_wait;
    }

    public int getWin() {
        return win;
    }

    public void setWin(int win) {
        this.win = win;
    }

    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }
}
