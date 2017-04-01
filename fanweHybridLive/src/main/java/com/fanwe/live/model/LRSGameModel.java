package com.fanwe.live.model;


import java.util.List;

public class LRSGameModel {
    private String game_group_id;//狼人杀游戏公用频道
    private List<LRSUserModel> members;//成员
    private List<String> message;//游戏进程集合
    private String rule;//游戏规则

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

    public List<String> getMessage() {
        return message;
    }

    public void setMessage(List<String> message) {
        this.message = message;
    }

    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }
}
