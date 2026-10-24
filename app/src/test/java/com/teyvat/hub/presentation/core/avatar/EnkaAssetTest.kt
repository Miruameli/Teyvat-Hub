package com.teyvat.hub.presentation.core.avatar

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Verifies asset URL derivation.
 *
 * Wrong slugs mean missing character art, which the user sees as blank cards,
 * so the mapping rules are locked down explicitly.
 */
class EnkaAssetTest {

    @Test
    fun `simple name uses first token unchanged`() {
        assertEquals(
            "https://enka.network/ui/UI_AvatarIcon_Albedo.png",
            EnkaAsset.avatarIcon("Albedo"),
        )
    }

    @Test
    fun `title name collapses to known nickname`() {
        // Enka stores the ruler under "Raiden", not the full title.
        assertEquals(
            "https://enka.network/ui/UI_AvatarIcon_raiden.png",
            EnkaAsset.avatarIcon("Raiden Shogun"),
        )
    }

    @Test
    fun `multi word name uses first token`() {
        // "kaedehara kazuha" alias resolves to "kazuha" (lowercase, like other aliases)
        assertEquals(
            "https://enka.network/ui/UI_AvatarIcon_Side_kazuha.png",
            EnkaAsset.avatarSide("Kaedehara Kazuha"),
        )
    }

    @Test
    fun `apostrophes and spaces are stripped`() {
        val url = EnkaAsset.avatarIcon("Hu Tao")

        assertTrue(url!!.contains("hutao"))
        assertTrue(!url.contains("'"))
    }

    @Test
    fun `empty name returns null`() {
        assertNull(EnkaAsset.avatarIcon("   "))
    }

    @Test
    fun `side variant differs from icon variant`() {
        val icon = EnkaAsset.avatarIcon("Albedo")!!
        val side = EnkaAsset.avatarSide("Albedo")!!

        assertTrue(icon.contains("UI_AvatarIcon_"))
        assertTrue(side.contains("UI_AvatarIcon_Side_"))
    }
}