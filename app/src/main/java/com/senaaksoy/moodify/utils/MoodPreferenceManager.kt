package com.senaaksoy.moodify.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "mood_preferences")

@Singleton
class MoodPreferenceManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val notificationsEnabledKey = booleanPreferencesKey("notifications_enabled")

    val notificationsEnabled: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[notificationsEnabledKey] ?: true
        }

    suspend fun setNotificationsEnabled(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[notificationsEnabledKey] = enabled
        }

        if (enabled) {
            NotificationScheduler.scheduleAllNotifications(context)
        } else {
            NotificationScheduler.cancelAllNotifications(context)
        }
    }
}

