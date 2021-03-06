package com.example.lab4.db;

import androidx.annotation.NonNull;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.lab4.Group;

import java.util.List;

/**
 * Data access object (DAO), содержит методы доступа к данным. В нашим случае - SQL запросы к БД.
 * По аналогии с @Database классом, в случае работы Room мы описываем только сами SQL запросы, а маппинг
 * результатов выполнения запросов в сами объекты (например в методе {@link #getAll()}) выполняется
 * за нас библиотекой. Подробнее о построении DAO можно прочитать в оффициальной документации:
 * https://developer.android.com/training/data-storage/room/accessing-data.html
 */
@Dao
public interface GroupDao {
    @Query("SELECT * FROM groups")
    List<Group> getAll();

    @Insert
    void insert(@NonNull Group group);


}
