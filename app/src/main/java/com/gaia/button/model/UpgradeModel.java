package com.gaia.button.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.gaia.button.net.BaseResult;

/**
 * 2021/3/24 15:34
 */

public class UpgradeModel implements Parcelable {
    public int code;
    public String msg;
    public Data data;
    public static class Data implements Parcelable {

        public int id;
        public String deviceName;
        public String deviceCode;
        public String binPath;
        public String binVersion;
        public boolean isLatestStableVersion;

        protected Data(Parcel in) {
            id = in.readInt();
            deviceName = in.readString();
            deviceCode = in.readString();
            binPath = in.readString();
            binVersion = in.readString();
            isLatestStableVersion = in.readByte() != 0;
        }

        public final Creator<UpgradeModel> CREATOR = new Creator<UpgradeModel>() {
            @Override
            public UpgradeModel createFromParcel(Parcel in) {
                return new UpgradeModel(in);
            }

            @Override
            public UpgradeModel[] newArray(int size) {
                return new UpgradeModel[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(id);
            dest.writeString(deviceName);
            dest.writeString(deviceCode);
            dest.writeString(binPath);
            dest.writeString(binVersion);
            dest.writeByte((byte) (isLatestStableVersion ? 1 : 0));
        }
    }

    protected UpgradeModel(Parcel in) {
        code = in.readInt();
        msg = in.readString();
        data = in.readParcelable(Data.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(code);
        dest.writeString(msg);
        dest.writeParcelable(data, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<UpgradeModel> CREATOR = new Creator<UpgradeModel>() {
        @Override
        public UpgradeModel createFromParcel(Parcel in) {
            return new UpgradeModel(in);
        }

        @Override
        public UpgradeModel[] newArray(int size) {
            return new UpgradeModel[size];
        }
    };
}
