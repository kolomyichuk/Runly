package kolomyichuk.runly.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ThemePreferences

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ProfilePreferences