package com.example.hyodorbros.di

import android.content.Context
import androidx.room.Room
import com.example.hyodorbros.room.dao.DDayDao
import com.example.hyodorbros.room.database.DDayDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
object RoomModule {

    @Provides
    @Singleton
    fun provideDDayDatabase(@ApplicationContext context: Context): DDayDatabase =
        Room.databaseBuilder(context, DDayDatabase::class.java, "dday_table")
            .build()

    @Provides
    fun provideDDayDao(@ApplicationContext context: Context): DDayDao =
        provideDDayDatabase(context).ddayDao()

}