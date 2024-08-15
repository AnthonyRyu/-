package com.example.hyodorbros.di

import com.example.hyodorbros.data.repo.DDayRepository
import com.example.hyodorbros.data.repo.DDayRepositoryImpl
import com.example.hyodorbros.data.repo.FirebaseRepository
import com.example.hyodorbros.data.repo.FirebaseRepositoryImpl
import com.example.hyodorbros.data.source.remote.FirebaseRemoteDataSource
import com.example.hyodorbros.data.source.remote.FirebaseRemoteDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
abstract class RepositoryModule {

    @Singleton
    @Binds
    abstract fun ddayRepository(dDayRepositoryImpl: DDayRepositoryImpl): DDayRepository

    @Singleton
    @Binds
    abstract fun bindFirebaseRepository(firebaseRepositoryImpl: FirebaseRepositoryImpl): FirebaseRepository

    @Singleton
    @Binds
    abstract fun bindFirebaseRemoteDataSource(firebaseRemoteDataSourceImpl: FirebaseRemoteDataSourceImpl): FirebaseRemoteDataSource

}