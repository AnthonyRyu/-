package com.example.hyodorbros.room.dao

import androidx.room.*
import com.example.hyodorbros.room.entity.DDayEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DDayDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: DDayEntity): Long

    @Delete
    suspend fun delete(item: DDayEntity): Int

    @Query("SELECT * FROM dday_table ORDER BY date ASC")
    fun getAll(): Flow<List<DDayEntity>>

    @Update
    suspend fun update(model: DDayEntity): Int

    @Query("SELECT * FROM dday_table WHERE uid = :uid")
    suspend fun getEntity(uid: Int?): DDayEntity
}