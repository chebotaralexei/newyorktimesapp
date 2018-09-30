package ru.chebotar.newyorktimesapp.data.network.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by alex on 20.10.17.
 */

public class User implements Parcelable {

    public static final String MALE = "M";
    public static final String FEMALE = "F";

    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("email")
    @Expose
    public String email;
    @SerializedName("sex")
    @Expose
    public String sex;
    @SerializedName("access_token")
    @Expose
    public String accessToken;
    @SerializedName("refresh_token")
    @Expose
    public String refreshToken;
    @SerializedName("favourites_count")
    @Expose
    public Integer favouritesCount;
    @SerializedName("orders_count")
    @Expose
    public Integer ordersCount;
    @SerializedName("phone")
    @Expose
    public String phone;
    @SerializedName("want_spam")
    @Expose
    public String wantSpam;
    @SerializedName("want_push")
    @Expose
    public String wantPush;
    @SerializedName("birthdate")
    @Expose
    public String birthdate;
    @SerializedName("social_networks")
    @Expose
    public List<String> socialNetworks;

    public User() {
    }

    protected User(Parcel in) {
        id = in.readString();
        name = in.readString();
        email = in.readString();
        sex = in.readString();
        accessToken = in.readString();
        refreshToken = in.readString();
        if (in.readByte() == 0) {
            favouritesCount = null;
        } else {
            favouritesCount = in.readInt();
        }
        if (in.readByte() == 0) {
            ordersCount = null;
        } else {
            ordersCount = in.readInt();
        }
        phone = in.readString();
        wantSpam = in.readString();
        wantPush = in.readString();
        birthdate = in.readString();
        socialNetworks = in.createStringArrayList();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(email);
        dest.writeString(sex);
        dest.writeString(accessToken);
        dest.writeString(refreshToken);
        if (favouritesCount == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(favouritesCount);
        }
        if (ordersCount == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(ordersCount);
        }
        dest.writeString(phone);
        dest.writeString(wantSpam);
        dest.writeString(wantPush);
        dest.writeString(birthdate);
        dest.writeStringList(socialNetworks);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public static String isMaleToString(boolean isMale) {
        return isMale ? MALE : FEMALE;
    }

    public boolean isVkBind() {
        if (socialNetworks != null) {
            for (String socialNetwork : socialNetworks) {
                String lowerCase = socialNetwork.toLowerCase();
                if ("vkontakte".equals(lowerCase) || "vk".equals(lowerCase)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isFacebookBind() {
        if (socialNetworks != null) {
            for (String socialNetwork : socialNetworks) {
                String lowerCase = socialNetwork.toLowerCase();
                if ("facebook".equals(lowerCase) || "fb".equals(lowerCase)) {
                    return true;
                }
            }
        }
        return false;
    }

}
