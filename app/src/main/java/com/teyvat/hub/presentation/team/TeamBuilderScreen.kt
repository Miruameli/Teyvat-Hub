package com.teyvat.hub.presentation.team

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.teyvat.hub.R
import com.teyvat.hub.domain.service.TeamValidation
import com.teyvat.hub.presentation.team.saved.SavedTeamRow
import com.teyvat.hub.presentation.team.saved.SavedTeamsHeader
import com.teyvat.hub.presentation.team.saved.TemplateRow
import com.teyvat.hub.presentation.team.saved.TemplatesHeader

/**
 * Team builder for up to four characters.
 *
 * The whole screen is one scrolling list. Slots, the name field, the notes,
 * the roster, and the stored teams are items of that single list, so no
 * section can be pushed out of reach when the content grows.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeamBuilderScreen(
    viewModel: TeamBuilderViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = { TopAppBar(title = { Text(stringResource(R.string.teams_title)) }) },
    ) { padding ->
        LazyColumn(modifier = Modifier.fillMaxSize().padding(padding)) {
            item { TeamSlotsHeader() }
            items(state.selected, key = { "slot-${it.id}" }) { character ->
                SelectedTeamRow(
                    character = character,
                    onRemove = { viewModel.onCharacterToggled(character) },
                )
            }

            item {
                TextField(
                    value = state.teamName,
                    onValueChange = viewModel::onTeamNameChange,
                    label = { Text(stringResource(R.string.teams_name_label)) },
                    modifier = Modifier.fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                )
            }

            if (state.validation is TeamValidation.Invalid) {
                item { ValidationError((state.validation as TeamValidation.Invalid).reason) }
            }

            item {
                Button(
                    onClick = viewModel::onSaveTeam,
                    enabled = state.validation is TeamValidation.Valid,
                    modifier = Modifier.padding(16.dp),
                ) {
                    Text(stringResource(R.string.teams_save))
                }
            }

            item { SynergyNotes(insights = state.insights) }

            item { RosterHeader() }
            items(state.roster, key = { "roster-${it.id}" }) { character ->
                RosterRow(
                    character = character,
                    isSelected = state.selected.any { it.id == character.id },
                    onToggle = { viewModel.onCharacterToggled(character) },
                )
            }

            if (state.savedTeams.isNotEmpty()) {
                item { SavedTeamsHeader() }
                items(state.savedTeams, key = { "saved-${it.id}" }) { team ->
                    SavedTeamRow(team = team, onDelete = { viewModel.onDeleteTeam(team.id) })
                }
            }

            if (state.templates.isNotEmpty()) {
                item { TemplatesHeader() }
                items(state.templates, key = { "template-${it.id}" }) { team ->
                    TemplateRow(team = team)
                }
            }
        }
    }
}
