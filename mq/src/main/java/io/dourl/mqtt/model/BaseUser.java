package io.dourl.mqtt.model;

import android.os.Parcel;
import android.text.TextUtils;

import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import io.dourl.mqtt.base.BaseObject;
import io.dourl.mqtt.bean.UserModel;
import io.dourl.mqtt.model.customenum.ManagerType;

/**
 * 基础User类
 * Created by zhangpeng on 16/1/6.
 */
public class BaseUser extends UserModel implements BaseObject {

   // public List<GameModel> apps;
   // @JsonAdapter(BooleanJsonAdapter.class)
    protected Boolean is_block;
    public String sort;  //for sort
    //private List<CoinModel> coin_list;
    public int google;
    @SerializedName("no_password")
   // @JsonAdapter(BooleanJsonAdapter.class)
    public boolean noPassword;
    public String reward_max_to;
    public String reward_max_type;
    public boolean isSelect = false;
    public String alias;
    public String levelname;
    public String levellogo;
   // @JsonAdapter(BooleanJsonAdapter.class)
    public boolean trans_enable = false;
    public String hidMobile;


    //0=普通成员，1=群主，2=管理员
    private ManagerType managetype;

    private ClanAuthType clan_spec;

    //@JsonAdapter(BooleanJsonAdapter.class)
    public boolean chat_enable = true;

    public BaseUser() {
    }

    public ManagerType getManagerType() {
        if (managetype == null)
            return ManagerType.NONE;
        return managetype;
    }

    public void setManagetype(ManagerType managetype) {
        this.managetype = managetype;
    }

    public ClanAuthType getClan_spec() {
        if (clan_spec == null)
            return ClanAuthType.normal;
        return clan_spec;
    }

    public void setClan_spec(ClanAuthType clan_spec) {
        this.clan_spec = clan_spec;
    }

    public String getDisplayName() {
        if (!TextUtils.isEmpty(alias)) {
            return alias;
        } else {
            return getFullNameOrUserName();
        }
    }

    public boolean isBlock() {
        if (is_block != null) {
            return is_block;
        } else {
            return false;
        }
    }

    public void setIs_block(Boolean is_block) {
        this.is_block = is_block;
    }

    /*public List<CoinModel> getCoin_list() {
        return coin_list;
    }

    public void setCoin_list(List<CoinModel> coin_list) {
        if (this.coin_list == null) {
            this.coin_list = new ArrayList<>();
        }
        this.coin_list.clear();
        this.coin_list.addAll(coin_list);
    }*/

    public enum ClanAuthType implements BaseObject {
        @SerializedName("0")//普通
                normal(0),
        @SerializedName("1")//普通与付费
                vip(1),
        @SerializedName("3")//三种均可
                all(3),
        @SerializedName("2")//普通与付费
                payment(2);

        public int value;

        public String getStringValue() {
            return String.valueOf(value);
        }

        ClanAuthType(int value) {
            this.value = value;
        }

        public static ClanAuthType valueof(int value) {
            ClanAuthType type = normal;
            if (value == 1) {
                type = vip;
            } else if (value == 2) {
                type = payment;
            } else if (value == 3) {
                type = all;
            }
            return type;
        }

    }

    public boolean hasBindPhone() {
        return !TextUtils.isEmpty(mobile);
    }

    public boolean hasBind() {
        return !TextUtils.isEmpty(email) || !TextUtils.isEmpty(mobile);
    }

    public boolean isAddAuth() {
        return google != 0;
    }

    public boolean isOpenAuth() {
        return google == 1;
    }

    public String getAlias() {
        if (TextUtils.isEmpty(alias)) {
            return getFullNameOrUserName();
        } else {
            return alias;
        }
    }

    public String getSessionId() {
        return "u" + uid;
    }

    public String getDisplayMobile() {
        if (TextUtils.isEmpty(hidMobile)) {
            return mobile;
        } else {
            return hidMobile;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        //dest.writeTypedList(this.apps);
        dest.writeValue(this.isBlock());
        dest.writeString(this.sort);
        //dest.writeTypedList(this.coin_list);
        dest.writeInt(this.google);
        dest.writeByte(this.noPassword ? (byte) 1 : (byte) 0);
        dest.writeString(this.reward_max_to);
        dest.writeString(this.reward_max_type);
        dest.writeByte(this.isSelect ? (byte) 1 : (byte) 0);
        dest.writeString(this.alias);
        dest.writeString(this.levelname);
        dest.writeString(this.levellogo);
        dest.writeByte(this.trans_enable ? (byte) 1 : (byte) 0);
        dest.writeString(this.hidMobile);
        dest.writeInt(this.managetype == null ? -1 : this.managetype.ordinal());
        dest.writeInt(this.clan_spec == null ? -1 : this.clan_spec.ordinal());
    }

    protected BaseUser(Parcel in) {
        super(in);
        //this.apps = in.createTypedArrayList(GameModel.CREATOR);
        this.setIs_block((Boolean) in.readValue(Boolean.class.getClassLoader()));
        this.sort = in.readString();
       // this.coin_list = (in.createTypedArrayList(CoinModel.CREATOR));
        this.google = in.readInt();
        this.noPassword = in.readByte() != 0;
        this.reward_max_to = in.readString();
        this.reward_max_type = in.readString();
        this.isSelect = in.readByte() != 0;
        this.alias = in.readString();
        this.levelname = in.readString();
        this.levellogo = in.readString();
        this.trans_enable = in.readByte() != 0;
        this.hidMobile = in.readString();
        int tmpManagetype = in.readInt();
        this.managetype = tmpManagetype == -1 ? null : ManagerType.values()[tmpManagetype];
        int tmpClan_spec = in.readInt();
        this.clan_spec = tmpClan_spec == -1 ? null : ClanAuthType.values()[tmpClan_spec];
    }

    public static final Creator<BaseUser> CREATOR = new Creator<BaseUser>() {
        @Override
        public BaseUser createFromParcel(Parcel source) {
            return new BaseUser(source);
        }

        @Override
        public BaseUser[] newArray(int size) {
            return new BaseUser[size];
        }
    };
}
