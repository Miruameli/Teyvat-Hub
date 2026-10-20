package com.teyvat.hub.presentation.team

import com.teyvat.hub.application.CharacterRepository
import com.teyvat.hub.application.TeamRepository
import com.teyvat.hub.domain.model.Character
import com.teyvat.hub.domain.model.CharacterRole
import com.teyvat.hub.domain.model.Element
import com.teyvat.hub.domain.model.Rarity
import com.teyvat.hub.domain.model.Team
import com.teyvat.hub.domain.model.WeaponType
import com.teyvat.hub.domain.service.TeamSynergyAnalyzer
import com.teyvat.hub.domain.service.TeamValidator
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Shared fixtures for the team builder tests.
 *
 * The fakes live here so each test file stays focused on one concern and
 * stays inside the file size limit.
 */

/** Builds a character with a fixed identity and an optional element. */
internal fun character(id: String, element: Element = Element.PYRO) = Character(
    id = id,
    name = "Character $id",
    element = element,
    weaponType = WeaponType.SWORD,
    rarity = Rarity.FIVE,
    region = "Mondstadt",
    role = CharacterRole.MAIN_DPS,
)

/** Repository double that records every saved team. */
internal class RecordingTeamRepository : TeamRepository {
    val savedTeams = mutableListOf<Team>()

    override fun observeSaved(): Flow<List<Team>> = flow { emit(emptyList()) }
    override fun observeTemplates(): Flow<List<Team>> = flow { emit(emptyList()) }
    override suspend fun save(team: Team) { savedTeams += team }
    override suspend fun delete(id: String) = Unit
}

/** Repository double for a roster that is always empty. */
internal class EmptyCharacterRepository : CharacterRepository {
    override fun observeRoster(): Flow<List<Character>> = flow { emit(emptyList()) }
    override fun observeFavorites(): Flow<List<Character>> = flow { emit(emptyList()) }
    override fun search(query: String): Flow<List<Character>> = flow { emit(emptyList()) }
    override suspend fun toggleFavorite(id: String) = Unit
}

/** Builds a ViewModel wired to the recording repositories. */
internal fun teamBuilderViewModel(
    teamRepository: TeamRepository = RecordingTeamRepository(),
    characterRepository: CharacterRepository = EmptyCharacterRepository(),
): TeamBuilderViewModel = TeamBuilderViewModel(
    teamRepository = teamRepository,
    characterRepository = characterRepository,
    validator = TeamValidator(),
    analyzer = TeamSynergyAnalyzer(),
)
