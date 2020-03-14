package com.example.lab4;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.core.util.ObjectsCompat;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Для передачи самописных объектов через {@link android.content.Intent} или
 * {@link android.os.Bundle}, необходимо реализовать интерфейс {@link Parcelable}. В нём описывается
 * как сохранить и восстановить объект используя примитивные свойства (String, int и т.д.).
 */
@Entity(tableName = "students")
public class Student extends ListItem implements Parcelable {


	@PrimaryKey(autoGenerate = true)
	public int id;
	@NonNull
	@ColumnInfo(name = "first_name")
	public String firstName;
	@NonNull
	@ColumnInfo(name = "second_name")
	public String secondName;
	@NonNull
	@ColumnInfo(name = "last_name")
	public String lastName;
	@NonNull
	@ColumnInfo(name = "group_name")
	public String groupName;

	public Student(@NonNull String firstName, @NonNull String secondName, @NonNull String lastName, @NonNull String groupName) {
		this.lastName = lastName;
		this.firstName = firstName;
		this.secondName = secondName;
		this.groupName = groupName;
	}

	protected Student(Parcel in) {
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
		return lastName.equals(student.lastName) &&
				firstName.equals(student.firstName) &&
				secondName.equals(student.secondName);
	}

	@Override
	public int hashCode() {
		return ObjectsCompat.hash(lastName, firstName, secondName, groupName);
	}
	@Override
	public int getType() {
		return TYPE_STUDENT;
	}
}
