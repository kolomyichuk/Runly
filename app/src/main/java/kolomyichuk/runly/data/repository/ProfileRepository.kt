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

    suspend fun saveProfileImage(uri: Uri): File? {
        val oldFileName = preferencesDataStore.getProfileImageFileName()
        val newFile = imageStorage.saveImage(uri)

        newFile?.let { file ->
            preferencesDataStore.saveProfileImageFileName(file.name)
            oldFileName?.let { imageStorage.deleteFile(it) }
        }
        return newFile
    }

    suspend fun loadProfileImage(): File? {
        val fileName = preferencesDataStore.getProfileImageFileName()
        return if (fileName != null) {
            imageStorage.getFileByName(fileName)
        } else {
            null
        }
    }
}

