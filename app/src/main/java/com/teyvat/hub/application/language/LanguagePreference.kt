package com.teyvat.hub.application.language

import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.first
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext

/**
 * The languages the app ships with.
 *
 * The identifier is the language tag used by the platform, so it can be handed
 * straight to the locale APIs without a lookup table.
 */
enum class AppLanguage(val tag: String) {
    INDONESIAN("id"),
    ENGLISH("en"),
    ;

    companion object {
        /** The language used when nothing has been chosen yet. */
        val DEFAULT: AppLanguage = INDONESIAN

        /** Resolves a stored tag, falling back to the default when unknown. */
        fun fromTag(tag: String?): AppLanguage =
            entries.firstOrNull { it.tag == tag } ?: DEFAULT
    }
}

/** Where the chosen language is stored on disk. */
private val Context.languageDataStore: DataStore<Preferences> by preferencesDataStore(
    name = "language_preferences",
)

/**
 * Stores and reads the chosen language.
 *
 * The preference is written on the device only; nothing is sent anywhere. A
 * value that is not recognised falls back to the default rather than leaving
 * the app in a language it has no strings for.
 */
@Singleton
class LanguagePreference @Inject constructor(
    @ApplicationContext private val context: Context,
) {

    /**
     * Reads the chosen language.
     *
     * A failure to read the store is treated as "nothing chosen", because a
     * missing preference must not stop the app from starting.
     */
    suspend fun read(): AppLanguage = try {
        AppLanguage.fromTag(context.languageDataStore.data.first()[KEY_LANGUAGE])
    } catch (_: Exception) {
        AppLanguage.DEFAULT
    }

    /** Stores the chosen language. */
    suspend fun write(language: AppLanguage) {
        context.languageDataStore.edit { preferences ->
            preferences[KEY_LANGUAGE] = language.tag
        }
    }

    private companion object {
        val KEY_LANGUAGE = stringPreferencesKey("language")
    }
}
