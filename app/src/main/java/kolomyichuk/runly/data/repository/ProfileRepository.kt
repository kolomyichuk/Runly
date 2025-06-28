package kolomyichuk.runly.data.repository

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

class ProfileRepository(
    private val firebaseAuth: FirebaseAuth
) {
    suspend fun getCurrentUserName(): String? = withContext(Dispatchers.IO) {
        firebaseAuth.currentUser?.displayName
    }

    suspend fun getCurrentPhotoUrl(): String? = withContext(Dispatchers.IO) {
        firebaseAuth.currentUser?.photoUrl?.toString()
    }
}

