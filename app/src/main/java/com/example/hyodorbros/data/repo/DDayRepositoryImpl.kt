package com.example.hyodorbros.data.repo

import com.example.hyodorbros.room.dao.DDayDao
import com.example.hyodorbros.room.entity.DDayEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DDayRepositoryImpl @Inject constructor(private val dDayDao: DDayDao) : DDayRepository {

    override val totalDDayCount: Flow<Int>
        get() = dDayDao.getAll().map { it.size }
    override val dDayEntityList: Flow<List<DDayEntity>>
        get() = dDayDao.getAll()

    override suspend fun insertDDay(item: DDayEntity): Boolean =
        try {
            val result = dDayDao.insert(item)
            result > 0
        } catch (e: Exception) {
            false
        }

    override suspend fun deleteDDay(item: DDayEntity): Boolean =
        try {
            val result = dDayDao.delete(item)
            result >= 0
        } catch (e: Exception) {
            false
        }

    override suspend fun updateDDay(item: DDayEntity): Boolean =
        try {
            val result = dDayDao.update(item)
            result >= 0
        } catch (e: Exception) {
            false
        }

    override suspend fun getEntity(uid: Int): DDayEntity? =
        try {
            dDayDao.getEntity(uid)
        } catch (e: Exception) {
            null
        }
}