package com.alurwa.common.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class DispatcherIO

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class SessionPreferences