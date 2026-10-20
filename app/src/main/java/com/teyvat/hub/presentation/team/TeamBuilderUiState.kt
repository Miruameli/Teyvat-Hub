package com.teyvat.hub.presentation.team

import com.teyvat.hub.domain.model.Character
import com.teyvat.hub.domain.model.Team
import com.teyvat.hub.domain.service.SynergyInsight
import com.teyvat.hub.domain.service.TeamValidation

/**
 * Builder state: the current selection, its validation, and stored teams.
 *
 * [validation] carries the reason a team cannot be saved so the screen can
 * explain it instead of silently dropping the action.
 *
 * The party size limit lives in the domain layer
 * ([com.teyvat.hub.domain.service.TeamValidator.canAdd]); it is deliberately
 * not duplicated here, so the rule shown and the rule enforced stay the same.
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
