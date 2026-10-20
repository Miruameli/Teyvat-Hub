package com.teyvat.hub.domain.team

import com.teyvat.hub.domain.character.Character
import com.teyvat.hub.domain.character.CharacterRole
import com.teyvat.hub.domain.character.Element
import com.teyvat.hub.domain.character.Rarity
import com.teyvat.hub.domain.character.WeaponType
import org.junit.Assert.assertTrue
import org.junit.Test

/** Verifies synergy notes reflect a team's real elements and roles. */
class TeamSynergyAnalyzerTest {

    private val analyzer = TeamSynergyAnalyzer()

    private fun character(
        id: String,
        element: Element,
        role: CharacterRole,
    ) = Character(
        id = id,
        name = "Character $id",
        element = element,
        weaponType = WeaponType.SWORD,
        rarity = Rarity.FIVE,
        region = "Mondstadt",
        role = role,
    )

    @Test
    fun `pyro and hydro identifies vaporize`() {
        val team = listOf(
            character("a", Element.PYRO, CharacterRole.MAIN_DPS),
            character("b", Element.HYDRO, CharacterRole.SUB_DPS),
            character("c", Element.ANEMO, CharacterRole.HEALER),
        )

        val insights = analyzer.analyze(team)

        assertTrue(insights.any { it.headline.contains("Vaporize") })
    }

    @Test
    fun `hydro and dendro identifies bloom`() {
        val team = listOf(
            character("a", Element.DENDRO, CharacterRole.SUB_DPS),
            character("b", Element.HYDRO, CharacterRole.SUPPORT),
        )

        val insights = analyzer.analyze(team)

        assertTrue(insights.any { it.headline.contains("Bloom") })
    }

    @Test
    fun `healer is reported when present`() {
        val team = listOf(character("a", Element.PYRO, CharacterRole.HEALER))

        val insights = analyzer.analyze(team)

        assertTrue(insights.any { it.headline.contains("Penyembuh") })
    }

    @Test
    fun `missing sustain is reported`() {
        val team = listOf(character("a", Element.PYRO, CharacterRole.MAIN_DPS))

        val insights = analyzer.analyze(team)

        assertTrue(insights.any { it.headline.contains("Tanpa sustain") })
    }

    @Test
    fun `too many dps is flagged`() {
        val team = listOf(
            character("a", Element.PYRO, CharacterRole.MAIN_DPS),
            character("b", Element.ELECTRO, CharacterRole.MAIN_DPS),
            character("c", Element.CRYO, CharacterRole.SUB_DPS),
            character("d", Element.HYDRO, CharacterRole.SUB_DPS),
        )

        val insights = analyzer.analyze(team)

        assertTrue(insights.any { it.headline.contains("Terlalu banyak DPS") })
    }

    @Test
    fun `empty team returns no insights`() {
        val insights = analyzer.analyze(emptyList())

        assertTrue(insights.isEmpty())
    }
}
