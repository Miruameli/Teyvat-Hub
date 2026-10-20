package com.teyvat.hub.presentation.team

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teyvat.hub.application.CharacterRepository
import com.teyvat.hub.application.TeamRepository
import com.teyvat.hub.domain.model.Character
import com.teyvat.hub.domain.model.Team
import com.teyvat.hub.domain.service.TeamSynergyAnalyzer
import com.teyvat.hub.domain.service.TeamValidation
import com.teyvat.hub.domain.service.TeamValidator
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * Drives the team builder screen.
 *
 * Validation runs here rather than in the screen so the rule cannot be
 * bypassed by a second entry point; the screen only renders the outcome.
 */
@HiltViewModel
class TeamBuilderViewModel @Inject constructor(
    private val teamRepository: TeamRepository,
    private val characterRepository: CharacterRepository,
    private val validator: TeamValidator,
    private val analyzer: TeamSynergyAnalyzer,
) : ViewModel() {

    private val selection = MutableStateFlow<List<Character>>(emptyList())
    private val teamName = MutableStateFlow("")

    val uiState: StateFlow<TeamBuilderUiState> = combine(
        selection,
        teamName,
        combine(
            teamRepository.observeSaved(),
            teamRepository.observeTemplates(),
            characterRepository.observeRoster(),
        ) { saved, templates, roster -> Triple(saved, templates, roster) },
    ) { members, name, stored ->
        val (saved, templates, roster) = stored
        TeamBuilderUiState(
            selected = members,
            roster = roster,
            savedTeams = saved,
            templates = templates,
            teamName = name,
            validation = validate(members, name),
            insights = analyzer.analyze(members),
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = TeamBuilderUiState(),
    )

    /**
     * Adds or removes a character from the current selection.
     *
     * The party size is capped by the domain rule rather than here, so the
     * limit cannot drift from the one the validator enforces when saving.
     */
    fun onCharacterToggled(character: Character) {
        val current = selection.value
        if (current.any { it.id == character.id }) {
            selection.value = current.filterNot { it.id == character.id }
            return
        }
        if (!validator.canAdd(current)) return
        selection.value = current + character
    }

    fun onTeamNameChange(value: String) {
        teamName.value = value
    }

    fun onSaveTeam() {
        val members = selection.value
        val name = teamName.value.trim()
        when (val result = validate(members, name)) {
            is TeamValidation.Invalid -> return
            TeamValidation.Valid -> {
                viewModelScope.launch {
                    teamRepository.save(
                        Team(
                            id = "team-" + System.currentTimeMillis(),
                            name = name,
                            characterIds = members.map { it.id },
                            synergyNotes = analyzer.analyze(members)
                                .joinToString(separator = "; ") { it.headline },
                        ),
                    )
                    selection.value = emptyList()
                    teamName.value = ""
                }
            }
        }
    }

    fun onDeleteTeam(id: String) {
        viewModelScope.launch { teamRepository.delete(id) }
    }

    private fun validate(members: List<Character>, name: String): TeamValidation =
        validator.validate(
            Team(
                id = "draft",
                name = name,
                characterIds = members.map { it.id },
            ),
        )
}
