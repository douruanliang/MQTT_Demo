package io.dourl.mqtt.model.typeadapter;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;

import io.dourl.mqtt.base.BaseObject;


public class BaseClanModel implements BaseObject, Parcelable {

    public String id;
    public String name;
    public String color;
    protected AuthType spec_clan;

    public enum AuthType implements BaseObject {
        @SerializedName("-1")
        NONE(-1),
        @SerializedName("0")//普通
                normal(0),
        @SerializedName("1")//vip
                vip(1),
        @SerializedName("2")//付费
                payment(2);

        public int value;

        public String getStringValue() {
            return String.valueOf(value);
        }

        AuthType(int value) {
            this.value = value;
        }

        public static AuthType valueof(int value) {
            AuthType type = normal;
            if (value == 1) {
                type = vip;
            } else if (value == 2) {
                type = payment;
            } else {

            }
            return type;
        }

    }

    public BaseClanModel() {

    }

    public AuthType getSpec_clan() {
        if (spec_clan == null)
            return AuthType.NONE;
        return spec_clan;
    }

    public void setSpec_clan(AuthType spec_clan) {
        this.spec_clan = spec_clan;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeString(this.color);
        dest.writeInt(this.spec_clan == null ? -1 : this.spec_clan.ordinal());
    }

    protected BaseClanModel(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.color = in.readString();
        int tmpSpec_clan = in.readInt();
        this.spec_clan = tmpSpec_clan == -1 ? null : AuthType.values()[tmpSpec_clan];
    }

    public static final Creator<BaseClanModel> CREATOR = new Creator<BaseClanModel>() {
        @Override
        public BaseClanModel createFromParcel(Parcel source) {
            return new BaseClanModel(source);
        }

        @Override
        public BaseClanModel[] newArray(int size) {
            return new BaseClanModel[size];
        }
    };
}
