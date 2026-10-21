package com.teyvat.hub.presentation.core.theme

import androidx.annotation.StringRes
import com.teyvat.hub.R
import com.teyvat.hub.application.language.AppLanguage

/**
 * Player facing names for the languages the app ships with.
 *
 * A language is named in its own language, which is the convention every
 * language picker follows, so the label is not translated.
 */

/** The name of the language, written as its speakers write it. */
@StringRes
fun AppLanguage.labelRes(): Int = when (this) {
    AppLanguage.INDONESIAN -> R.string.language_indonesian
    AppLanguage.ENGLISH -> R.string.language_english
}
