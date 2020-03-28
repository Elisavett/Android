package com.example.lab2;

import android.os.Parcel;
import android.os.Parcelable;

public class Progress implements Parcelable {
    private String progressName;
    private String progressValue;

    public Progress(String progressName, String progressValue) {
        this.progressName = progressName;
        this.progressValue = progressValue;
    }

    public Progress(Parcel in) {
        progressName = in.readString();
        progressValue = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(progressName);
        dest.writeString(progressValue);

    }

    public static final Creator<Progress> CREATOR = new Creator<Progress>() {
        @Override
        public Progress createFromParcel(Parcel in) {
            return new Progress(in);
        }

        @Override
        public Progress[] newArray(int size) {
            return new Progress[size];
        }
    };
    public String getProgressName()
    {
        return progressName;
    }
    public String getProgressValue()
    {
        return progressValue;
    }
}
