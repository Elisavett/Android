package com.example.lab4.db;

import androidx.annotation.NonNull;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.lab4.Student;
import com.example.lab4.StudentGroup;

import java.util.List;

/**
 * Data access object (DAO), содержит методы доступа к данным. В нашим случае - SQL запросы к БД.
 * По аналогии с @Database классом, в случае работы Room мы описываем только сами SQL запросы, а маппинг
 * результатов выполнения запросов в сами объекты (например в методе {@link #getAll()}) выполняется
 * за нас библиотекой. Подробнее о построении DAO можно прочитать в оффициальной документации:
 * https://developer.android.com/training/data-storage/room/accessing-data.html
 */
@Dao
public interface StudentDao {
    @Query("SELECT * FROM students")
    List<Student> getAll();

    @Insert
    void insert(@NonNull Student student);

    @Query(
            "SELECT COUNT(*) FROM students WHERE " +
                    "first_name = :firstName AND " +
                    "second_name = :secondName AND " +
                    "last_name = :lastName AND " +
                    "group_id = :groupId"
    )
    int count(@NonNull String firstName, @NonNull String secondName, @NonNull String lastName, @NonNull int groupId);
    @Query("SELECT first_name || ' ' || second_name || ' ' || last_name as studentName, " +
            "group_name as groupName " +
            "FROM students as s INNER JOIN groups as g " +
            "ON s.group_id = g.id")
    List<StudentGroup> getStudentsWithGroups();
}
