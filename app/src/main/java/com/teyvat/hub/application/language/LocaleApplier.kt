package com.teyvat.hub.application.language

import javax.inject.Inject
import javax.inject.Singleton
import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import dagger.hilt.android.qualifiers.ApplicationContext

/**
 * Applies the chosen language to the running app.
 *
 * The platform locale API only exists from Android 13, so the compatible path
 * is used instead: it stores the choice and applies it on every version the
 * app supports, from 7 upwards.
 */
@Singleton
class LocaleApplier @Inject constructor(
    @ApplicationContext private val context: Context,
) {

    /**
     * Applies [language] to every activity of the app.
     *
     * The platform is told as well, so a player who changes the language from
     * the system settings sees the same result in the app.
     */
    fun apply(language: AppLanguage) {
        AppCompatDelegate.setApplicationLocales(
            LocaleListCompat.forLanguageTags(language.tag),
        )
    }

    /**
     * The language the app is currently showing.
     *
     * The platform is the source of truth, because a player can also change
     * the language from the system settings. The stored preference is only a
     * fallback for the case where the platform has no locale set yet.
     */
    fun current(fallback: AppLanguage): AppLanguage {
        val platform = AppCompatDelegate.getApplicationLocales()
        if (platform.isEmpty) return fallback
        val tag = platform[0]?.toLanguageTag().orEmpty()
        return AppLanguage.fromTag(tag.takeWhile { it != '-' }.ifEmpty { null })
    }

    /**
     * The locale the app is running with, read from the configuration.
     *
     * It is what the resources actually follow, so it is used to check that
     * the applied locale took effect.
     */
    fun activeLocale(): String {
        val configuration: Configuration = context.resources.configuration
        val locales = configuration.locales
        return if (locales.isEmpty) "" else locales[0]?.toLanguageTag().orEmpty()
    }
}
