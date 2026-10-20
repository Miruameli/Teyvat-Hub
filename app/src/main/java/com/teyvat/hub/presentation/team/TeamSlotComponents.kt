package com.teyvat.hub.presentation.team

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
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
import com.teyvat.hub.domain.model.Character
import com.teyvat.hub.domain.service.SynergyInsight

/**
 * The four party slots with the members currently assigned.
 *
 * The slot list is fixed at the in-game party size so the user can see at a
 * glance how many slots are still free.
 */
@Composable
fun SelectedTeamSlot(
    selected: List<Character>,
    onRemove: (Character) -> Unit,
) {
    Text(
        text = stringResource(R.string.teams_current),
        style = MaterialTheme.typography.titleSmall,
        modifier = Modifier.padding(start = 16.dp, top = 12.dp),
    )

    if (selected.isEmpty()) {
        Text(
            text = stringResource(R.string.teams_empty_selection),
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(start = 16.dp),
        )
        return
    }

    selected.forEach { character ->
        ListItem(
            headlineContent = { Text(character.name) },
            supportingContent = {
                Text("${character.element.key} · ${character.role.key}")
            },
            trailingContent = {
                IconButton(onClick = { onRemove(character) }) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = stringResource(R.string.teams_remove),
                    )
                }
            },
            modifier = Modifier.padding(bottom = 4.dp),
        )
    }
}

/** Actionable notes about the current party composition. */
@Composable
fun SynergyNotes(insights: List<SynergyInsight>) {
    if (insights.isEmpty()) return

    Text(
        text = stringResource(R.string.teams_synergy),
        style = MaterialTheme.typography.titleSmall,
        modifier = Modifier.padding(start = 16.dp, top = 12.dp),
    )

    Column(modifier = Modifier.padding(start = 16.dp)) {
        insights.forEach { insight ->
            Text(
                text = insight.headline,
                style = MaterialTheme.typography.bodyMedium,
            )
            Text(
                text = insight.detail,
                style = MaterialTheme.typography.bodySmall,
            )
        }
    }
}
