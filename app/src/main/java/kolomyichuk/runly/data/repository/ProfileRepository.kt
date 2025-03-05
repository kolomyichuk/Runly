package kolomyichuk.runly.data.repository

import android.net.Uri
import kolomyichuk.runly.data.local.datastore.ProfilePreferencesDataStore
import kolomyichuk.runly.data.local.storage.ImageStorage
import kotlinx.coroutines.flow.Flow
import java.io.File
import javax.inject.Inject

class ProfileRepository @Inject constructor(
    private val preferencesDataStore: ProfilePreferencesDataStore,
    private val imageStorage: ImageStorage
) {
    fun getUsername(): Flow<String> = preferencesDataStore.username

    suspend fun saveUsername(username: String) {
        preferencesDataStore.saveUsername(username)
    }

    suspend fun saveImage(uri: Uri):File? {
        return imageStorage.saveImage(uri)
    }

    suspend fun loadImage(): File? {
        return imageStorage.loadImage()
    }
}

