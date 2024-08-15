package com.example.hyodorbros.data.repo

import com.example.hyodorbros.room.entity.DDayEntity
import kotlinx.coroutines.flow.Flow

interface DDayRepository {

    val totalDDayCount: Flow<Int>

    val dDayEntityList: Flow<List<DDayEntity>>

    suspend fun insertDDay(item: DDayEntity): Boolean
    suspend fun deleteDDay(item: DDayEntity): Boolean
    suspend fun updateDDay(item: DDayEntity): Boolean
    suspend fun getEntity(uid: Int): DDayEntity?
}