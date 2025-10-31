package com.senaaksoy.moodify.repository

import android.content.Context
import android.net.Uri
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.senaaksoy.moodify.model.User
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.tasks.await
import java.io.File
import java.io.FileOutputStream
import java.util.UUID
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage,
    @ApplicationContext private val context: Context
) {
    val currentUser get() = auth.currentUser

    suspend fun signUp(email: String, password: String): AuthResult =
        auth.createUserWithEmailAndPassword(email, password).await()

    suspend fun signIn(email: String, password: String): AuthResult =
        auth.signInWithEmailAndPassword(email, password).await()

    suspend fun sendEmailVerification() {
        auth.currentUser?.sendEmailVerification()?.await()
    }

    suspend fun sendPasswordResetEmail(email: String) {
        auth.sendPasswordResetEmail(email).await()
    }

    suspend fun uploadProfileImage(uid: String, imageUri: Uri): String {
        val inputStream = context.contentResolver.openInputStream(imageUri)
            ?: throw Exception("Resim yüklenemedi")

        val tempFile = File(context.cacheDir, "temp_${UUID.randomUUID()}.jpg")

        try {
            inputStream.use { input ->
                FileOutputStream(tempFile).use { output ->
                    input.copyTo(output)
                }
            }

            val storageRef = storage.reference.child("profile_images/$uid.jpg")
            val uploadTask = storageRef.putFile(Uri.fromFile(tempFile)).await()
            val downloadUrl = storageRef.downloadUrl.await().toString()

            return downloadUrl
        } finally {
            if (tempFile.exists()) {
                tempFile.delete()
            }
        }
    }

    suspend fun saveProfileImageUrl(uid: String, url: String) {
        firestore.collection("users").document(uid)
            .update("profileImageUrl", url)
            .await()
    }

    suspend fun getProfileImageUrl(uid: String): String? {
        return try {
            val doc = firestore.collection("users").document(uid).get().await()
            doc.getString("profileImageUrl")
        } catch (e: Exception) {
            null
        }
    }

    fun logOut() {
        auth.signOut()
    }

    suspend fun saveUserFirestore(user: User) {
        firestore.collection("users").document(user.uid).set(user).await()
    }

    suspend fun registerUser(email: String, password: String, username: String) {
        val authResult = signUp(email, password)
        val uid = authResult.user?.uid ?: throw Exception("UID bulunamadı")
        val user = User(uid = uid, email = email, username = username)
        saveUserFirestore(user)
        sendEmailVerification()
    }

    suspend fun verifyPasswordResetCode(oobCode: String): String {
        return auth.verifyPasswordResetCode(oobCode).await()
    }

    suspend fun confirmPasswordReset(oobCode: String, newPassword: String) {
        auth.confirmPasswordReset(oobCode, newPassword).await()
    }

    suspend fun signInWithGoogle(account: GoogleSignInAccount): AuthResult {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        return auth.signInWithCredential(credential).await()
    }

    suspend fun isUserRegistered(email: String): Boolean {
        val snapshot = firestore.collection("users")
            .whereEqualTo("email", email)
            .get()
            .await()
        return !snapshot.isEmpty
    }

    suspend fun saveGoogleUser(account: GoogleSignInAccount) {
        val uid = auth.currentUser?.uid ?: throw Exception("UID bulunamadı")
        val email = account.email ?: throw Exception("Email bulunamadı")
        val displayName = account.displayName ?: "User"

        val user = User(
            uid = uid,
            email = email,
            username = displayName
        )
        saveUserFirestore(user)
    }
}