package com.alurwa.berkelas.di

import android.content.Context
import android.content.SharedPreferences
import com.alurwa.common.di.ApplicationScope
import com.alurwa.common.di.DispatcherIO
import com.alurwa.common.di.SessionPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @DispatcherIO
    @Singleton
    @Provides
    fun provideDispatcherIO() = Dispatchers.IO

    @ApplicationScope
    @Provides
    @Singleton
    fun applicationScope(): CoroutineScope = CoroutineScope(SupervisorJob())

    @SessionPreferences
    @Singleton
    @Provides
    fun provideSessionSharedPreferences(
        @ApplicationContext context: Context
    ): SharedPreferences = context.getSharedPreferences(
        "com.alurwa.berkelas.session_user",
        Context.MODE_PRIVATE
    )
}