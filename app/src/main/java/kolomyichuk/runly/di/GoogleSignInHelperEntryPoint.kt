package kolomyichuk.runly.di

import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kolomyichuk.runly.ui.screens.signin.GoogleSignInHelper

@EntryPoint
@InstallIn(SingletonComponent::class)
interface GoogleSignInHelperEntryPoint {
    fun googleSignInHelper(): GoogleSignInHelper
}
