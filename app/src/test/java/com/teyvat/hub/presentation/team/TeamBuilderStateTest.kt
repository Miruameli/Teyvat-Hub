package com.teyvat.hub.presentation.team

import com.teyvat.hub.domain.model.Character
import com.teyvat.hub.domain.model.CharacterRole
import com.teyvat.hub.domain.model.Element
import com.teyvat.hub.domain.model.Rarity
import com.teyvat.hub.domain.model.WeaponType
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/** Verifies the selection rules the builder screen relies on. */
class TeamBuilderStateTest {

    @Test
    fun `a character cannot be added twice`() {
        val state = TeamBuilderUiState(selected = listOf(character("a")))

        assertFalse(state.canAdd(character("a")))
    }

    @Test
    fun `no character can be added once the party is full`() {
        val state = TeamBuilderUiState(
            selected = listOf(
                character("a"),
                character("b"),
                character("c"),
                character("d"),
            ),
        )

        assertFalse(state.canAdd(character("e")))
    }

    @Test
    fun `a new character can be added to an empty selection`() {
        val state = TeamBuilderUiState()

        assertTrue(state.canAdd(character("a")))
    }

    private fun character(id: String, element: Element = Element.PYRO) = Character(
        id = id,
        name = "Character $id",
        element = element,
        weaponType = WeaponType.SWORD,
        rarity = Rarity.FIVE,
        region = "Mondstadt",
        role = CharacterRole.MAIN_DPS,
    )
}
