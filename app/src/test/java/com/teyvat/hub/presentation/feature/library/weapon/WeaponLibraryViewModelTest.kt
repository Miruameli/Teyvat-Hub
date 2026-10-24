package com.teyvat.hub.presentation.feature.library.weapon

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import com.teyvat.hub.application.repository.WeaponRepository
import com.teyvat.hub.domain.character.Rarity
import com.teyvat.hub.domain.character.WeaponType
import com.teyvat.hub.domain.equipment.Weapon
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

/**
 * Verifies the weapon library's only state rule.
 *
 * The repository owns what "of this type" means and the screen owns how a row
 * looks, so the behaviour worth pinning here is the toggle: tapping the family
 * that is already active clears the filter, the same rule the roster uses, so
 * the two lists cannot disagree.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class WeaponLibraryViewModelTest {

    private val dispatcher = UnconfinedTestDispatcher()
    private val weapons = MutableStateFlow(
        listOf(
            weapon("sword-1", WeaponType.SWORD),
            weapon("bow-1", WeaponType.BOW),
            weapon("catalyst-1", WeaponType.CATALYST),
        ),
    )
    private val repository = object : WeaponRepository {
        override fun observeAll(): Flow<List<Weapon>> = weapons
        override fun observeByType(type: WeaponType): Flow<List<Weapon>> = MutableStateFlow(
            weapons.value.filter { it.type == type },
        )
    }
    private val viewModel = WeaponLibraryViewModel(repository)

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `the library starts with every weapon shown`() = runTest {
        val state = viewModel.uiState.first { it.weapons.isNotEmpty() }

        assertEquals(3, state.weapons.size)
        assertNull(state.typeFilter)
    }

    @Test
    fun `choosing a family narrows the list to that family`() = runTest {
        val state = viewModel.uiState.first { it.weapons.isNotEmpty() }.also {
            viewModel.onTypeSelected(WeaponType.BOW)
        }
        val narrowed = viewModel.uiState.first { it.typeFilter != null }

        assertEquals(WeaponType.BOW, state.typeFilter)
        assertEquals(1, narrowed.weapons.size)
        assertEquals(WeaponType.BOW, narrowed.weapons.single().type)
    }

    @Test
    fun `choosing the active family again clears the filter`() = runTest {
        viewModel.uiState.first { it.weapons.isNotEmpty() }
        viewModel.onTypeSelected(WeaponType.BOW)
        viewModel.uiState.first { it.typeFilter != null }
        viewModel.onTypeSelected(WeaponType.BOW)

        val cleared = viewModel.uiState.first { it.typeFilter == null }

        assertEquals(3, cleared.weapons.size)
    }

    private fun weapon(id: String, type: WeaponType) = Weapon(
        id = id,
        name = id,
        type = type,
        rarity = Rarity.FOUR,
        baseAttack = 42,
        subStat = "ATK",
        subStatValue = "6.0%",
        passive = "",
    )
}