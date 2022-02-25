package io.dourl.mqtt.bean;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;


import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Keep;
import org.greenrobot.greendao.annotation.Unique;

import io.dourl.mqtt.base.BaseObject;

/**
 * 基础User类
 * Created by zhangpeng on 16/1/6.
 */
@Entity
public class UserModel implements BaseObject, Parcelable {

    @Id
    @Unique
    protected String uid;
    protected String username;
    protected String fullname;
    protected String email;
    protected String mobile;
    protected String avatar;
    protected String avatar_thumb;
    protected int push_notify;
    protected String share_link;
    protected int age;
    protected String country;
    protected String province;
    protected String city;
    protected int level;
    protected int game_count;
    protected String birthday;
    protected String color;
    protected int gender;
    protected int is_friend;
    protected String country_code;
    protected int city_code;
    protected int save_static;//是否自动保存到相册
    protected int now_exp;
    protected int max_exp;
    protected int id_change_times;
    protected int from_third;
    protected Integer is_cryptomate_agent = 0;//是否为代理商0：不是 1：是

    /**
     * 性别 0:未知 1:男 2:女
     */
    public static final int GENDER_UNKOWN = 0;
    public static final int GENDER_BOY = 1;
    public static final int GENDER_GIRL = 2;

    public String getFullName() {
        return fullname;
    }

    public boolean pushNotify() {
        return push_notify != 0;
    }

    public void setPushNotify(boolean pushNotify) {
        push_notify = pushNotify ? 1 : 0;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public boolean isAgent() {
        if (is_cryptomate_agent == null)
            return false;
        return is_cryptomate_agent == 1;
    }

    public String getName() {
        if (fullname == null || fullname.length() == 0) {
            return username;
        }
        return fullname;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getPush_notify() {
        return push_notify;
    }

    public void setPush_notify(int push_notify) {
        this.push_notify = push_notify;
    }

    public String getShare_link() {
        return share_link;
    }

    public void setShare_link(String share_link) {
        this.share_link = share_link;
    }

    /**
     * 优先返回fullName，为空则返回UserName
     */
    public String getFullNameOrUserName() {
        if (fullname != null && fullname.length() != 0) {
            return fullname;
        } else if (username != null && username.length() != 0) {
            return username;
        } else {
            return "";
        }
    }

    public String getFullAddress() {
        String address ="";
        if (TextUtils.isEmpty(country) && TextUtils.isEmpty(province) && TextUtils.isEmpty(city)) {
           // address = AppContextUtil.getContext().getString(R.string.unknow_address);
        } else {
           // String addressFormat = AppContextUtil.getContext().getString(R.string.format_address);
           // address = String.format(addressFormat, country, province, city);
        }
        return address;
    }

    public String getAvatar_thumb() {
        return avatar_thumb;
    }

    public void setAvatar_thumb(String avatar_thumb) {
        this.avatar_thumb = avatar_thumb;
    }

    public UserModel() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserModel baseUser = (UserModel) o;

        return uid.equals(baseUser.uid);
    }


    @Override
    public int hashCode() {
        return uid.hashCode();
    }


    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getAge() {
        return this.age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getCountry() {
        return this.country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getProvince() {
        return this.province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return this.city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getLevel() {
        return this.level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getGame_count() {
        return this.game_count;
    }

    public void setGame_count(int game_count) {
        this.game_count = game_count;
    }

    public String getBirthday() {
        return this.birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getColor() {
        return this.color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public void setIsFriend(boolean isFriend) {
        this.is_friend = isFriend ? 1 : 0;
    }

    public boolean isFriend() {
        return is_friend == 1;
    }

    public int getIs_friend() {
        return this.is_friend;
    }

    public void setIs_friend(int is_friend) {
        this.is_friend = is_friend;
    }

    public String getCountry_code() {
        return this.country_code;
    }

    public void setCountry_code(String country_code) {
        this.country_code = country_code;
    }

    public int getCity_code() {
        return this.city_code;
    }


    public void setCity_code(int city_code) {
        this.city_code = city_code;
    }

    public int getSave_static() {
        return this.save_static;
    }

    public void setSave_static(int save_static) {
        this.save_static = save_static;
    }

    public boolean saveStatic() {
        return save_static != 0;
    }

    public void setSaveStatic(boolean saveStatic) {
        save_static = saveStatic ? 1 : 0;
    }

    public int getNow_exp() {
        return this.now_exp;
    }

    public void setNow_exp(int now_exp) {
        this.now_exp = now_exp;
    }

    public int getMax_exp() {
        return this.max_exp;
    }

    public void setMax_exp(int max_exp) {
        this.max_exp = max_exp;
    }

    public int getid_change_times() {
        return this.id_change_times;
    }

    public void setid_change_times(int id_change_times) {
        this.id_change_times = id_change_times;
    }

    public int getFrom_third() {
        return this.from_third;
    }

    public void setFrom_third(int from_third) {
        this.from_third = from_third;
    }


    public String getMobile() {
        return this.mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }


    public int getId_change_times() {
        return this.id_change_times;
    }

    public void setId_change_times(int id_change_times) {
        this.id_change_times = id_change_times;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.uid);
        dest.writeString(this.username);
        dest.writeString(this.fullname);
        dest.writeString(this.email);
        dest.writeString(this.mobile);
        dest.writeString(this.avatar);
        dest.writeString(this.avatar_thumb);
        dest.writeInt(this.push_notify);
        dest.writeString(this.share_link);
        dest.writeInt(this.age);
        dest.writeString(this.country);
        dest.writeString(this.province);
        dest.writeString(this.city);
        dest.writeInt(this.level);
        dest.writeInt(this.game_count);
        dest.writeString(this.birthday);
        dest.writeString(this.color);
        dest.writeInt(this.gender);
        dest.writeInt(this.is_friend);
        dest.writeString(this.country_code);
        dest.writeInt(this.city_code);
        dest.writeInt(this.save_static);
        dest.writeInt(this.now_exp);
        dest.writeInt(this.max_exp);
        dest.writeInt(this.id_change_times);
        dest.writeInt(this.from_third);
        dest.writeValue(this.is_cryptomate_agent);
    }

    public Integer getIs_cryptomate_agent() {
        return this.is_cryptomate_agent;
    }

    public void setIs_cryptomate_agent(Integer is_cryptomate_agent) {
        this.is_cryptomate_agent = is_cryptomate_agent;
    }

    protected UserModel(Parcel in) {
        this.uid = in.readString();
        this.username = in.readString();
        this.fullname = in.readString();
        this.email = in.readString();
        this.mobile = in.readString();
        this.avatar = in.readString();
        this.avatar_thumb = in.readString();
        this.push_notify = in.readInt();
        this.share_link = in.readString();
        this.age = in.readInt();
        this.country = in.readString();
        this.province = in.readString();
        this.city = in.readString();
        this.level = in.readInt();
        this.game_count = in.readInt();
        this.birthday = in.readString();
        this.color = in.readString();
        this.gender = in.readInt();
        this.is_friend = in.readInt();
        this.country_code = in.readString();
        this.city_code = in.readInt();
        this.save_static = in.readInt();
        this.now_exp = in.readInt();
        this.max_exp = in.readInt();
        this.id_change_times = in.readInt();
        this.from_third = in.readInt();
        this.is_cryptomate_agent = (Integer) in.readValue(Integer.class.getClassLoader());
    }

    @Generated(hash = 49380387)
    public UserModel(String uid, String username, String fullname, String email, String mobile,
            String avatar, String avatar_thumb, int push_notify, String share_link, int age,
            String country, String province, String city, int level, int game_count, String birthday,
            String color, int gender, int is_friend, String country_code, int city_code,
            int save_static, int now_exp, int max_exp, int id_change_times, int from_third,
            Integer is_cryptomate_agent) {
        this.uid = uid;
        this.username = username;
        this.fullname = fullname;
        this.email = email;
        this.mobile = mobile;
        this.avatar = avatar;
        this.avatar_thumb = avatar_thumb;
        this.push_notify = push_notify;
        this.share_link = share_link;
        this.age = age;
        this.country = country;
        this.province = province;
        this.city = city;
        this.level = level;
        this.game_count = game_count;
        this.birthday = birthday;
        this.color = color;
        this.gender = gender;
        this.is_friend = is_friend;
        this.country_code = country_code;
        this.city_code = city_code;
        this.save_static = save_static;
        this.now_exp = now_exp;
        this.max_exp = max_exp;
        this.id_change_times = id_change_times;
        this.from_third = from_third;
        this.is_cryptomate_agent = is_cryptomate_agent;
    }

    public static final Creator<UserModel> CREATOR = new Creator<UserModel>() {
        @Override
        public UserModel createFromParcel(Parcel source) {
            return new UserModel(source);
        }

        @Override
        public UserModel[] newArray(int size) {
            return new UserModel[size];
        }
    };
}
