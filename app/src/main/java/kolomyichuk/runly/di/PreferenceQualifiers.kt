package kolomyichuk.runly.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class SettingsPreferences

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ProfilePreferences