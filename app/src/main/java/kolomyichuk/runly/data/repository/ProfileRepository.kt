package kolomyichuk.runly.data.repository

import android.net.Uri
import kolomyichuk.runly.data.local.datastore.ProfilePreferencesDataStore
import kolomyichuk.runly.data.local.storage.ImageStorage
import kotlinx.coroutines.flow.Flow

class ProfileRepository (
    private val preferencesDataStore: ProfilePreferencesDataStore,
    private val imageStorage: ImageStorage
) {
    fun getUsername(): Flow<String> = preferencesDataStore.username

    suspend fun saveUsername(username: String) {
        preferencesDataStore.saveUsername(username)
    }

    suspend fun saveProfileImage(uri: Uri): String? {
        val oldFileName = preferencesDataStore.getProfileImageFileName()
        val newFile = imageStorage.saveImage(uri)

        newFile?.let { file ->
            preferencesDataStore.saveProfileImageFileName(file.name)
            oldFileName?.let { imageStorage.deleteFile(it) }
        }
        return newFile?.absolutePath
    }

    suspend fun loadProfileImage(): String? {
        val fileName = preferencesDataStore.getProfileImageFileName()
        return fileName?.let {
            imageStorage.getFileByName(it).absolutePath
        }
    }
}

