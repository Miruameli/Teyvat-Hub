package com.teyvat.hub.application.language

import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Verifies how a stored language tag is resolved.
 *
 * The store itself needs a device, so the part that can be checked on the
 * JVM is the resolution of a tag into a language.
 */
class AppLanguageTest {

    @Test
    fun `indonesian tag resolves to indonesian`() {
        assertEquals(AppLanguage.INDONESIAN, AppLanguage.fromTag("id"))
    }

    @Test
    fun `english tag resolves to english`() {
        assertEquals(AppLanguage.ENGLISH, AppLanguage.fromTag("en"))
    }

    @Test
    fun `unknown tag falls back to the default`() {
        assertEquals(AppLanguage.DEFAULT, AppLanguage.fromTag("fr"))
    }

    @Test
    fun `missing tag falls back to the default`() {
        assertEquals(AppLanguage.DEFAULT, AppLanguage.fromTag(null))
    }

    @Test
    fun `default language is indonesian`() {
        assertEquals(AppLanguage.INDONESIAN, AppLanguage.DEFAULT)
    }
}
