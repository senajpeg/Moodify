package com.senaaksoy.moodify.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.senaaksoy.moodify.model.FavoriteTrack
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FavoritesRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) {
    private fun getUserFavoritesCollection() =
        auth.currentUser?.uid?.let { userId ->
            firestore.collection("users")
                .document(userId)
                .collection("favorites")
        }

    suspend fun addToFavorites(track: FavoriteTrack): Boolean {
        return try {
            getUserFavoritesCollection()
                ?.document(track.trackId.toString())
                ?.set(track)
                ?.await()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    suspend fun removeFromFavorites(trackId: Long): Boolean {
        return try {
            getUserFavoritesCollection()
                ?.document(trackId.toString())
                ?.delete()
                ?.await()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    suspend fun isFavorite(trackId: Long): Boolean {
        return try {
            val doc = getUserFavoritesCollection()
                ?.document(trackId.toString())
                ?.get()
                ?.await()
            doc?.exists() == true
        } catch (e: Exception) {
            false
        }
    }

    fun getFavorites(): Flow<List<FavoriteTrack>> = callbackFlow {
        val collection = getUserFavoritesCollection()

        if (collection == null) {
            trySend(emptyList())
            close()
            return@callbackFlow
        }

        val listener = collection
            .orderBy("addedAt", com.google.firebase.firestore.Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(emptyList())
                    return@addSnapshotListener
                }

                val favorites = snapshot?.documents?.mapNotNull { doc ->
                    doc.toObject(FavoriteTrack::class.java)
                } ?: emptyList()

                trySend(favorites)
            }

        awaitClose { listener.remove() }
    }
}
