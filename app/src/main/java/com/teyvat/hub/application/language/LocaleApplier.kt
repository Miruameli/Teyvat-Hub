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

    /** Applies [language] to every activity of the app. */
    fun apply(language: AppLanguage) {
        AppCompatDelegate.setApplicationLocales(
            LocaleListCompat.forLanguageTags(language.tag),
        )
    }

    /**
     * The language the app is currently showing.
     *
     * It is read from the configuration rather than from the stored
     * preference, because that is what the resources actually follow.
     */
    fun current(): AppLanguage {
        val configuration: Configuration = context.resources.configuration
        val locales = configuration.locales
        val tag = if (locales.isEmpty) "" else locales[0]?.toLanguageTag().orEmpty()
        return AppLanguage.fromTag(tag.takeWhile { it != '-' }.ifEmpty { null })
    }
}
