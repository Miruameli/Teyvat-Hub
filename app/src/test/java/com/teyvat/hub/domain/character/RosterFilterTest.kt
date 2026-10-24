package com.teyvat.hub.domain.character

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Verifies the roster narrowing rules, independent of any screen.
 *
 * The rules live in the domain so a search, an element, and the favourites
 * filter cannot disagree about which characters are shown, and so every
 * combination is testable without a device.
 */
class RosterFilterTest {

    private val kerri = character("kerri", Element.PYRO, isFavorite = true)
    private val finnia = character("finnia", Element.CRYO)
    private val roan = character("roan", Element.PYRO)

    @Test
    fun `an empty filter keeps every character`() {
        val result = listOf(kerri, finnia, roan).matching(RosterFilter())

        assertEquals(listOf("kerri", "finnia", "roan"), result.map { it.id })
    }

    @Test
    fun `a query matches case insensitively`() {
        val result = listOf(kerri, finnia, roan).matching(RosterFilter(query = "ROA"))

        assertEquals(listOf("roan"), result.map { it.id })
    }

    @Test
    fun `an element filter keeps only that element`() {
        val result = listOf(kerri, finnia, roan).matching(RosterFilter(element = Element.PYRO))

        assertEquals(listOf("kerri", "roan"), result.map { it.id })
    }

    @Test
    fun `the favourites filter keeps only favourites`() {
        val result = listOf(kerri, finnia, roan).matching(RosterFilter(onlyFavorites = true))

        assertEquals(listOf("kerri"), result.map { it.id })
    }

    @Test
    fun `every filter applies at once`() {
        val filter = RosterFilter(
            query = "ker",
            element = Element.PYRO,
            onlyFavorites = true,
        )

        val result = listOf(kerri, finnia, roan).matching(filter)

        assertEquals(listOf("kerri"), result.map { it.id })
    }

    @Test
    fun `a filter that matches nothing returns nothing`() {
        val filter = RosterFilter(element = Element.DENDRO)

        val result = listOf(kerri, finnia, roan).matching(filter)

        assertTrue(result.isEmpty())
    }

    @Test
    fun `a blank query is treated as no text filtering`() {
        val result = listOf(kerri, finnia, roan).matching(RosterFilter(query = "   "))

        assertEquals(3, result.size)
    }

    private fun character(id: String, element: Element, isFavorite: Boolean = false) = Character(
        id = id,
        name = id,
        element = element,
        weaponType = WeaponType.SWORD,
        rarity = Rarity.FIVE,
        region = "Mondstadt",
        role = CharacterRole.MAIN_DPS,
        isFavorite = isFavorite,
    )
}
