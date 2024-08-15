package com.example.hyodorbros.room.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.hyodorbros.room.dao.DDayDao
import com.example.hyodorbros.room.entity.DDayEntity


@Database(entities = [DDayEntity::class], version = 1)
abstract class DDayDatabase : RoomDatabase() {
    abstract fun ddayDao(): DDayDao
}