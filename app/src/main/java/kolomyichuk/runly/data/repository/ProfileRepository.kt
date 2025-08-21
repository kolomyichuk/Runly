package kolomyichuk.runly.data.repository

import com.google.firebase.auth.FirebaseAuth

class ProfileRepository(
    private val firebaseAuth: FirebaseAuth
) {
    fun getCurrentUserName(): String? {
        return firebaseAuth.currentUser?.displayName
    }

    fun getCurrentPhotoUrl(): String? {
        return firebaseAuth.currentUser?.photoUrl?.toString()
    }
}
