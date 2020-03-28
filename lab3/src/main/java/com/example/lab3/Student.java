package com.example.lab3;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.core.util.ObjectsCompat;

/**
 * Для передачи самописных объектов через {@link android.content.Intent} или
 * {@link android.os.Bundle}, необходимо реализовать интерфейс {@link Parcelable}. В нём описывается
 * как сохранить и восстановить объект используя примитивные свойства (String, int и т.д.).
 */
public class Student extends ListItem implements Parcelable {

    private int currId = 0;

    @NonNull
    public int id;
    @NonNull
    public String firstName;
    @NonNull
    public String secondName;
    @NonNull
    public String lastName;
    @NonNull
    public String groupName;

    public Student(@NonNull String firstName, @NonNull String secondName, @NonNull String lastName, @NonNull String groupName) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.secondName = secondName;
        this.groupName = groupName;
        this.id = currId++;
    }

    protected Student(Parcel in) {
        id = in.readInt();
        firstName = in.readString();
        lastName = in.readString();
        secondName = in.readString();
        groupName = in.readString();
    }

    public static final Creator<Student> CREATOR = new Creator<Student>() {
        @Override
        public Student createFromParcel(Parcel in) {
            return new Student(in);
        }

        @Override
        public Student[] newArray(int size) {
            return new Student[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(firstName);
        dest.writeString(lastName);
        dest.writeString(secondName);
        dest.writeString(groupName);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Student)) return false;
        Student student = (Student) o;
        return id == student.id;
    }

    @Override
    public int hashCode() {
        return ObjectsCompat.hash(id, lastName, firstName, secondName, groupName);
    }

    @Override
    public int getType() {
        return TYPE_STUDENT;
    }
}