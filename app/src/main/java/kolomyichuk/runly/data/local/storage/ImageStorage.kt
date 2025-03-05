package kolomyichuk.runly.data.local.storage

import android.content.Context
import android.net.Uri
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject

class ImageStorage @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private fun getProfileImageFile(): File {
        // Генерація унікальної назви файлу на основі поточного часу
        val uniqueFileName = "profile_image_${System.currentTimeMillis()}.jpg"
        return File(context.filesDir, uniqueFileName)
    }

    // Збереження нового зображення з унікальним ім'ям файлу
    suspend fun saveImage(uri: Uri): File? {
        return withContext(Dispatchers.IO) {
            val file = getProfileImageFile()
            context.contentResolver.openInputStream(uri)?.use { inputStream ->
                file.outputStream().use { outputStream ->
                    inputStream.copyTo(outputStream) // Зберігаємо новий файл з унікальним ім'ям
                }
            }
            if (file.exists()) file else null
        }
    }

    //можливо видаляти попередні фото якщо фони є в історії?

    // Завантаження останнього зображення (необхідно зберігати шляхи до файлів)
    suspend fun loadImage(): File? {
        return withContext(Dispatchers.IO) {
            val files = context.filesDir.listFiles()
            // Сортуємо за часом створення файлів
            val lastFile = files?.maxByOrNull { it.lastModified() }
            lastFile
        }
    }
}

