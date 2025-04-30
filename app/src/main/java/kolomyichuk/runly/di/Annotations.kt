package kolomyichuk.runly.di

import javax.inject.Qualifier

// TODO Let's rename this file to 'PreferenceQualifiers'. It describes these annotations better.

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ThemePreferences

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ProfilePreferences