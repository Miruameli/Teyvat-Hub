package com.teyvat.hub.presentation.team

import com.teyvat.hub.domain.model.MAX_TEAM_SIZE
import com.teyvat.hub.domain.model.Character
import com.teyvat.hub.domain.model.Team
import com.teyvat.hub.domain.service.SynergyInsight
import com.teyvat.hub.domain.service.TeamValidation

/**
 * Builder state: the current selection, its validation, and stored teams.
 *
 * [validation] carries the reason a team cannot be saved so the screen can
 * explain it instead of silently dropping the action.
 */
data class TeamBuilderUiState(
    val selected: List<Character> = emptyList(),
    val roster: List<Character> = emptyList(),
    val savedTeams: List<Team> = emptyList(),
    val templates: List<Team> = emptyList(),
    val teamName: String = "",
    val validation: TeamValidation = TeamValidation.Valid,
    val insights: List<SynergyInsight> = emptyList(),
)

/** True when a character may be added to the current selection. */
fun TeamBuilderUiState.canAdd(character: Character): Boolean =
    selected.size < MAX_TEAM_SIZE && selected.none { it.id == character.id }
