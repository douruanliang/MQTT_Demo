package io.dourl.mqtt.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;
import com.google.gson.annotations.JsonAdapter;

import java.util.List;

import io.dourl.mqtt.base.BaseObject;

/**
 * Created by zhangheng on 2018/1/22.
 * 部落（群聊）
 */

public class ClanModel extends BaseClanModel implements BaseObject, Parcelable {

    public int leader;
    public String avatar;
    public int member_cnt;
    public List<String> game_icons;
    @JsonAdapter(BooleanJsonAdapter.class)
    public boolean msg_receive;
    @JsonAdapter(BooleanJsonAdapter.class)
    public boolean msg_top;
    public boolean hasAt = false;

    public ClanModel() {
        super();
    }

    public static ClanModel buildClan(String databaseValue) {
        Gson gson = new Gson();
        ClanModel baseMsgBody = null;
        if (databaseValue != null) {
            baseMsgBody = gson.fromJson(databaseValue, ClanModel.class);
        }
        return baseMsgBody;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(this.leader);
        dest.writeString(this.avatar);
        dest.writeInt(this.member_cnt);
        dest.writeStringList(this.game_icons);
        dest.writeByte(this.msg_receive ? (byte) 1 : (byte) 0);
        dest.writeByte(this.msg_top ? (byte) 1 : (byte) 0);
    }

    protected ClanModel(Parcel in) {
        super(in);
        this.leader = in.readInt();
        this.avatar = in.readString();
        this.member_cnt = in.readInt();
        this.game_icons = in.createStringArrayList();
        this.msg_receive = in.readByte() != 0;
        this.msg_top = in.readByte() != 0;
    }

    public static final Creator<ClanModel> CREATOR = new Creator<ClanModel>() {
        @Override
        public ClanModel createFromParcel(Parcel source) {
            return new ClanModel(source);
        }

        @Override
        public ClanModel[] newArray(int size) {
            return new ClanModel[size];
        }
    };
}
