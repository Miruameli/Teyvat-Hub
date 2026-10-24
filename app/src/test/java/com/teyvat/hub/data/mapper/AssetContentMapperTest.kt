package com.teyvat.hub.data.mapper

import com.teyvat.hub.data.content.CharacterRecord
import com.teyvat.hub.data.content.RegionRecord
import com.teyvat.hub.data.content.WeaponRecord
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Verifies the bundle to Room mapping that decides what a player sees.
 *
 * The bundle is authored separately from the app, so the interesting cases
 * are the ones a hand-written enum list would never hit: an unknown key, a
 * lowercase region that has to become a readable name, and a rarity this
 * build does not model.
 */
class AssetContentMapperTest {

    @Test
    fun `region names come from the bundle, not from the character row`() {
        val names = listOf(RegionRecord(key = "mondstadt", name = "Mondstadt"))

        val entity = character(region = "mondstadt").toEntity(names.toRegionNames())

        assertEquals("Mondstadt", entity!!.region)
    }

    @Test
    fun `unknown region key keeps the stored key rather than crashing`() {
        val entity = character(region = "snezhnaya")
            .toEntity(emptyMap())

        assertEquals("snezhnaya", entity!!.region)
    }

    @Test
    fun `an unknown element drops the row instead of mistaking its element`() {
        val entity = character(element = "abyss").toEntity(emptyMap())

        // A character filed under the wrong element is worse than a missing
        // one, so an unmapped key must not be written at all.
        assertNull(entity)
    }

    @Test
    fun `an unknown role drops the row`() {
        val entity = character(role = "archon").toEntity(emptyMap())

        assertNull(entity)
    }

    @Test
    fun `rarity maps to the modelled tiers`() {
        assertEquals(5, character(rarity = 5).toEntity(emptyMap())?.rarityStars)
        assertEquals(5, character(rarity = 9).toEntity(emptyMap())?.rarityStars)
        assertEquals(4, character(rarity = 4).toEntity(emptyMap())?.rarityStars)
        assertEquals(4, character(rarity = 1).toEntity(emptyMap())?.rarityStars)
    }

    @Test
    fun `weapon rows keep the values the screen sorts by`() {
        val entity = weapon().toEntity()

        assertEquals("favoniussword", entity!!.id)
        assertEquals("sword", entity.typeKey)
        assertEquals(4, entity.rarityStars)
        assertEquals(44, entity.baseAttack)
        assertTrue(entity.passive.isNotBlank())
    }

    @Test
    fun `an unknown weapon type drops the row`() {
        val entity = weapon(type = "chakram").toEntity()

        assertNull(entity)
    }

    private fun character(
        element: String = "cryo",
        role: String = "sub-dps",
        region: String = "mondstadt",
        rarity: Int = 4,
    ) = CharacterRecord(
        id = "kaeya",
        name = "Kaeya",
        element = element,
        weaponType = "sword",
        rarity = rarity,
        region = region,
        role = role,
    )

    private fun weapon(type: String = "sword") = WeaponRecord(
        id = "favoniussword",
        name = "Favonius Sword",
        type = type,
        rarity = 4,
        baseAttack = 44,
        subStat = "energy_recharge",
        subStatValue = "6.7%",
    )
}