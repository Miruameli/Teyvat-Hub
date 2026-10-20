package com.teyvat.hub.presentation.team

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
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
import com.teyvat.hub.domain.model.Character
import com.teyvat.hub.domain.service.TeamValidation

/**
 * Team builder for up to four characters.
 *
 * The character picker and the synergy notes live in separate files so each
 * one stays readable; this file owns the flow between them.
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
        Column(modifier = Modifier.fillMaxSize().padding(padding)) {
            SelectedTeamSlot(
                selected = state.selected,
                onRemove = viewModel::onCharacterToggled,
            )

            TextField(
                value = state.teamName,
                onValueChange = viewModel::onTeamNameChange,
                label = { Text(stringResource(R.string.teams_name_label)) },
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
            )

            if (state.validation is TeamValidation.Invalid) {
                Text(
                    text = (state.validation as TeamValidation.Invalid).reason,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(horizontal = 16.dp),
                )
            }

            Button(
                onClick = viewModel::onSaveTeam,
                enabled = state.validation is TeamValidation.Valid,
                modifier = Modifier.padding(16.dp),
            ) {
                Text(stringResource(R.string.teams_save))
            }

            SynergyNotes(insights = state.insights)

            CharacterPicker(
                roster = state.roster,
                selected = state.selected,
                onToggle = viewModel::onCharacterToggled,
            )
        }
    }
}
