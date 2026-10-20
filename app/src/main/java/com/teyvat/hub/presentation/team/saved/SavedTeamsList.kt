package com.teyvat.hub.presentation.team.saved

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

/** Heading above the saved teams. */
@Composable
fun SavedTeamsHeader() {
    Text(
        text = stringResource(R.string.teams_saved),
        style = MaterialTheme.typography.titleSmall,
        modifier = Modifier.padding(start = 16.dp, top = 12.dp),
    )
}

/** One saved team with a delete action. */
@Composable
fun SavedTeamRow(
    team: Team,
    onDelete: () -> Unit,
) {
    ListItem(
        headlineContent = { Text(team.name) },
        supportingContent = {
            Text("${team.characterIds.size} karakter")
        },
        trailingContent = {
            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = stringResource(R.string.teams_delete),
                )
            }
        },
    )
}

/** Heading above the built-in templates. */
@Composable
fun TemplatesHeader() {
    Text(
        text = stringResource(R.string.teams_templates),
        style = MaterialTheme.typography.titleSmall,
        modifier = Modifier.padding(start = 16.dp, top = 12.dp),
    )
}

/** One built-in team template. */
@Composable
fun TemplateRow(team: Team) {
    ListItem(
        headlineContent = { Text(team.name) },
        supportingContent = {
            Text(team.synergyNotes.ifBlank { "${team.characterIds.size} karakter" })
        },
    )
}
