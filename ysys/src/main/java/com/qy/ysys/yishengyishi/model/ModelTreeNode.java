package com.qy.ysys.yishengyishi.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by tony.chen on 2017/1/8.
 */

public class ModelTreeNode implements Parcelable {
//    {
//        “id”: 0,
//        ”familyId”: 8,
//        ”gender”: 0,
//        ”name”: ”游侠”,
//        ”userId”: 7,
//        ”uuid”: ”3b3bb68ac1a747b691db5c6a1ce15e90”,
//        ”rank”: 0,
//        ”isPartner”: 0,
//        ”partnerId”: 0,
//        ”parentUuid”: ”4a0af464daeb480eab41ee32388975c5”,
//        ”parentGender”: 0,
//        ”phone”: null
//    }


    private int id;
    private String familyId;
    private int gender;

    protected ModelTreeNode(Parcel in) {
        id = in.readInt();
        familyId = in.readString();
        gender = in.readInt();
        name = in.readString();
        userId = in.readInt();
        uuid = in.readString();
        rank = in.readInt();
        isPartner = in.readInt();
        partnerId = in.readInt();
        parentUuid = in.readString();
        parentGender = in.readInt();
        phone = in.readString();
    }

    public static final Creator<ModelTreeNode> CREATOR = new Creator<ModelTreeNode>() {
        @Override
        public ModelTreeNode createFromParcel(Parcel in) {
            return new ModelTreeNode(in);
        }

        @Override
        public ModelTreeNode[] newArray(int size) {
            return new ModelTreeNode[size];
        }
    };

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    private String name;
    private int userId;
    private String uuid;
    private int rank;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getParentGender() {
        return parentGender;
    }

    public void setParentGender(int parentGender) {
        this.parentGender = parentGender;
    }

    public String getParentUuid() {
        return parentUuid;
    }

    public void setParentUuid(String parentUuid) {
        this.parentUuid = parentUuid;
    }

    public int getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(int partnerId) {
        this.partnerId = partnerId;
    }

    public int getIsPartner() {
        return isPartner;
    }

    public void setIsPartner(int isPartner) {
        this.isPartner = isPartner;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getFamilyId() {
        return familyId;
    }

    public void setFamilyId(String familyId) {
        this.familyId = familyId;
    }

    public ModelTreeNode() {

    }

    private int isPartner;
    private int partnerId;
    private String parentUuid;
    private int parentGender;
    private String phone;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(familyId);
        dest.writeInt(gender);
        dest.writeString(name);
        dest.writeInt(userId);
        dest.writeString(uuid);
        dest.writeInt(rank);
        dest.writeInt(isPartner);
        dest.writeInt(partnerId);
        dest.writeString(parentUuid);
        dest.writeInt(parentGender);
        dest.writeString(phone);
    }
}
