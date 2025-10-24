package com.senaaksoy.moodify.repository

import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.senaaksoy.moodify.model.User
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val auth : FirebaseAuth,
    private val firestore: FirebaseFirestore
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



    fun logOut() {
        auth.signOut()
    }
    suspend fun saveUserFirestore(user: User) {
        firestore.collection("users").document(user.uid).set(user).await()
    }
    suspend fun registerUser(email: String, password: String, username: String) {
        // Firebase Auth ile kullanıcı oluştur
        val authResult = signUp(email, password)
        val uid = authResult.user?.uid ?: throw Exception("UID bulunamadı")
        val user = User(uid = uid, email = email, username = username)
        // Firestore'a kaydet
        saveUserFirestore(user)

        // Doğrulama maili gönder
        sendEmailVerification()
    }

    suspend fun verifyPasswordResetCode(oobCode: String): String {
        return auth.verifyPasswordResetCode(oobCode).await()
    }

    suspend fun confirmPasswordReset(oobCode: String, newPassword: String) {
        auth.confirmPasswordReset(oobCode, newPassword).await()
    }
    // Google Sign-In fonksiyonları
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