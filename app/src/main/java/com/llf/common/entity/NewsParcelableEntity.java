package com.llf.common.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by llf on 2017/8/18.
 * 弄一个实现Parcelable的实体类，性能比Serializable高很多
 */

public class NewsParcelableEntity implements Parcelable {
    private String username;
    private String nickname;
    private int age;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public NewsParcelableEntity(String username, String nickname, int age) {
        super();
        this.username = username;
        this.nickname = nickname;
        this.age = age;
    }

    /**
     * 读取，需要和写入的一一对应
     * @param in
     */
    protected NewsParcelableEntity(Parcel in) {
        username = in.readString();
        nickname=in.readString();
        age = in.readInt();
    }

    /**
     * 写入
     * @param dest
     * @param flags
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(username);
        dest.writeString(nickname);
        dest.writeInt(age);
    }

    /**
     * 这里默认返回0即可
     * @return
     */
    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<NewsParcelableEntity> CREATOR = new Creator<NewsParcelableEntity>() {
        /**
         * 从Parcel中读取数据
         */
        @Override
        public NewsParcelableEntity createFromParcel(Parcel in) {
            return new NewsParcelableEntity(in);
        }
        /**
         * 供外部类反序列化本类数组使用
         */
        @Override
        public NewsParcelableEntity[] newArray(int size) {
            return new NewsParcelableEntity[size];
        }
    };
}
