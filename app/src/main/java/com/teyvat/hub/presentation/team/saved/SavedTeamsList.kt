package com.teyvat.hub.presentation.team.saved

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.teyvat.hub.R
import com.teyvat.hub.domain.model.Team

/**
 * Teams the player already saved, with a delete action.
 *
 * Without this list a saved team would be invisible: the builder clears the
 * selection after saving, so the only way to see a team again is to read it
 * back from storage.
 */
@Composable
fun SavedTeamsList(
    savedTeams: List<Team>,
    onDelete: (String) -> Unit,
) {
    if (savedTeams.isEmpty()) return

    Text(
        text = stringResource(R.string.teams_saved),
        style = MaterialTheme.typography.titleSmall,
        modifier = Modifier.padding(start = 16.dp, top = 12.dp),
    )

    savedTeams.forEach { team ->
        ListItem(
            headlineContent = { Text(team.name) },
            supportingContent = {
                Text("${team.characterIds.size} karakter")
            },
            trailingContent = {
                IconButton(onClick = { onDelete(team.id) }) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = stringResource(R.string.teams_delete),
                    )
                }
            },
        )
    }
}

/** Built-in team templates shipped with the app. */
@Composable
fun TeamTemplates(templates: List<Team>) {
    if (templates.isEmpty()) return

    Text(
        text = stringResource(R.string.teams_templates),
        style = MaterialTheme.typography.titleSmall,
        modifier = Modifier.padding(start = 16.dp, top = 12.dp),
    )

    Column {
        templates.forEach { team ->
            ListItem(
                headlineContent = { Text(team.name) },
                supportingContent = {
                    Text(team.synergyNotes.ifBlank { "${team.characterIds.size} karakter" })
                },
            )
        }
    }
}
