package com.alurwa.berkelas.di

import com.alurwa.common.di.DispatcherIO
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @DispatcherIO
    @Singleton
    @Provides
    fun provideDispatcherIO() = Dispatchers.IO
}