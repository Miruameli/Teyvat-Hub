package com.teyvat.hub.domain.team

import com.teyvat.hub.domain.character.Character
import com.teyvat.hub.domain.character.CharacterRole
import com.teyvat.hub.domain.character.Element
import com.teyvat.hub.domain.character.Rarity
import com.teyvat.hub.domain.character.WeaponType
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/** Verifies team rules that protect storage from invalid members. */
class TeamValidatorTest {

    private val validator = TeamValidator()

    private fun character(id: String) = Character(
        id = id,
        name = "Character $id",
        element = Element.PYRO,
        weaponType = WeaponType.SWORD,
        rarity = Rarity.FIVE,
        region = "Mondstadt",
        role = com.teyvat.hub.domain.character.CharacterRole.MAIN_DPS,
    )

    private fun team(name: String, vararg ids: String) = Team(
        id = "t1",
        name = name,
        characterIds = ids.toList(),
    )

    @Test
    fun `valid four member team passes`() {
        val result = validator.validate(team("Vaporize", "a", "b", "c", "d"))

        assertTrue(result is TeamValidation.Valid)
    }

    @Test
    fun `team larger than four is rejected`() {
        val result = validator.validate(team("Too big", "a", "b", "c", "d", "e"))

        assertTrue(result is TeamValidation.Invalid)
    }

    @Test
    fun `duplicate characters are rejected`() {
        val result = validator.validate(team("Dupe", "a", "b", "a"))

        assertTrue(result is TeamValidation.Invalid)
    }

    @Test
    fun `empty team is rejected`() {
        val result = validator.validate(team("Empty"))

        assertTrue(result is TeamValidation.Invalid)
    }

    @Test
    fun `blank name is rejected`() {
        val result = validator.validate(team("  ", "a", "b"))

        assertTrue(result is TeamValidation.Invalid)
    }

    @Test
    fun `canAdd rejects when team already full`() {
        val full = listOf(
            character("a"),
            character("b"),
            character("c"),
            character("d"),
        )

        assertFalse(validator.canAdd(full))
    }

    @Test
    fun `canAdd accepts when slots remain`() {
        val partial = listOf(character("a"), character("b"))

        assertTrue(validator.canAdd(partial))
    }

    @Test
    fun `canAdd rejects duplicate members`() {
        val dupe = listOf(character("a"), character("a"))

        assertFalse(validator.canAdd(dupe))
    }
}
