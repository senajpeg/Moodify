package com.senaaksoy.moodify.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.senaaksoy.moodify.api.DeezerApi
import com.senaaksoy.moodify.model.DeezerSearchResponse
import com.senaaksoy.moodify.model.DeezerTracksResponse
import com.senaaksoy.moodify.model.MoodEntry
import kotlinx.coroutines.tasks.await
import java.util.Calendar
import javax.inject.Inject

class DeezerRepository @Inject constructor(
    private val api: DeezerApi,
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) {
    // Deezer API fonksiyonları
    suspend fun getPlaylistsByMood(mood: String): DeezerSearchResponse {
        return api.searchPlaylists(mood = mood)
    }

    suspend fun getPlaylistTracks(playlistId: Long): DeezerTracksResponse {
        return api.getPlaylistTracks(playlistId = playlistId)
    }

    // Mood kaydetme
    suspend fun saveMood(mood: String, note: String? = null) {
        val userId = auth.currentUser?.uid ?: throw IllegalStateException("User not logged in")

        val moodEntry = MoodEntry(
            mood = mood,
            note = note
        )

        firestore.collection("users")
            .document(userId)
            .collection("moods")
            .add(moodEntry)
            .await()
    }

    // Haftalık analiz
    suspend fun getWeeklyMoodAnalysis(): String {
        return try {
            val userId = auth.currentUser?.uid ?: return "Giriş yapılmamış"
            val calendar = Calendar.getInstance()

            // Son 7 günün başlangıcı
            calendar.add(Calendar.DAY_OF_YEAR, -7)
            val weekStart = calendar.time

            val moods = firestore.collection("users")
                .document(userId)
                .collection("moods")
                .whereGreaterThanOrEqualTo("timestamp", weekStart)
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .get()
                .await()
                .toObjects(MoodEntry::class.java)

            if (moods.isEmpty()) {
                return "Bu hafta henüz ruh hali kaydın yok"
            }

            // En çok tekrar eden mood'u bul
            val moodCounts = moods.groupingBy { it.mood }.eachCount()
            val dominantMood = moodCounts.maxByOrNull { it.value }?.key ?: "Mutlu"

            // Türkçe mood isimleri
            val moodName = when (dominantMood.lowercase()) {
                "happy", "mutlu" -> "mutlu"
                "sad", "üzgün" -> "üzgün"
                "chill", "sakin" -> "sakin"
                "energetic", "enerjik" -> "enerjik"
                else -> dominantMood
            }

            "Bu hafta en çok $moodName hissediyordun"
        } catch (e: Exception) {
            "Haftalık özet oluşturulamadı"
        }
    }
}