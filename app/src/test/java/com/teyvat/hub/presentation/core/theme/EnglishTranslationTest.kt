package com.teyvat.hub.presentation.core.theme

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.File

/**
 * Verifies the English translation is complete and really is a translation.
 *
 * The Indonesian strings are the source of truth, so the English file has to
 * cover every one of them. A missing entry would leave a label in the
 * previous language, which is exactly what the language choice is supposed to
 * prevent.
 */
class EnglishTranslationTest {

    private val source = File("app/src/main/res/values/strings.xml")
    private val translation = File("app/src/main/res/values-en/strings.xml")

    @Test
    fun `every indonesian string has an english counterpart`() {
        val missing = stringNames(source) - stringNames(translation)

        assertEquals("missing English strings: $missing", emptySet<String>(), missing)
    }

    @Test
    fun `the english file has no string the indonesian file lacks`() {
        val extra = stringNames(translation) - stringNames(source)

        assertEquals("English strings without an Indonesian counterpart: $extra", emptySet<String>(), extra)
    }

    @Test
    fun `the translation is not a copy of the source`() {
        val identical = stringNames(translation).filter { name ->
            valueOf(source, name) == valueOf(translation, name)
        }

        // A handful of names are the same in both languages, such as Geo or
        // Anemo, so a small overlap is expected. A wholesale copy is not.
        assertTrue(
            "the translation repeats ${identical.size} strings unchanged",
            identical.size < stringNames(translation).size / 2,
        )
    }

    private fun stringNames(file: File): Set<String> {
        val content = file.readText()
        return Regex("<string name=\"([^\"]+)\"").findAll(content)
            .map { it.groupValues[1] }
            .toSet()
    }

    private fun valueOf(file: File, name: String): String? {
        val content = file.readText()
        val match = Regex("<string name=\"$name\"[^>]*>(.*?)</string>", RegexOption.DOT_MATCH_ALL)
            .find(content)
        return match?.groupValues?.get(1)
    }
}
