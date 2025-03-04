package kolomyichuk.runly.data.repository

import android.graphics.Bitmap
import android.net.Uri
import kolomyichuk.runly.data.local.datastore.ProfilePreferencesDataStore
import kolomyichuk.runly.data.local.storage.ImageStorage
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ProfileRepository @Inject constructor(
    private val preferencesDataStore: ProfilePreferencesDataStore,
    private val imageStorage: ImageStorage
) {
    fun getUsername(): Flow<String> = preferencesDataStore.username

    suspend fun saveUsername(username: String) {
        preferencesDataStore.saveUsername(username)
    }

    fun saveImage(uri: Uri):Boolean {
        return imageStorage.saveImage(uri)
    }

    fun loadImage(): Bitmap? {
        return imageStorage.loadImage()
    }
}

