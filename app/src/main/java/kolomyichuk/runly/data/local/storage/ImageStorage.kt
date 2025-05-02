package kolomyichuk.runly.data.local.storage

import android.content.Context
import android.net.Uri
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

class ImageStorage(
    @ApplicationContext private val context: Context
) {
    suspend fun getFileByName(fileName: String): File {
        return withContext(Dispatchers.IO) {
            File(context.filesDir, fileName)
        }
    }

    suspend fun saveImage(uri: Uri): File? {
        return withContext(Dispatchers.IO) {
            val file = File(context.filesDir, "profile_image_${System.currentTimeMillis()}.jpg")
            context.contentResolver.openInputStream(uri)?.use { input ->
                file.outputStream().use { output ->
                    input.copyTo(output)
                }
            }
            if (file.exists()) file else null
        }
    }


    suspend fun deleteFile(fileName: String) {
        val file = getFileByName(fileName)
        if (file.exists()) {
            file.delete()
        }
    }
}

