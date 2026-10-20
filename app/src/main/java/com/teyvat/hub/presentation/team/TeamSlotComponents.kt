package com.teyvat.hub.presentation.team

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Checkbox
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
import com.teyvat.hub.domain.character.Character
import com.teyvat.hub.domain.team.SynergyInsight
import com.teyvat.hub.presentation.theme.labelRes

/** Heading above the party slots. */
@Composable
fun TeamSlotsHeader() {
    SectionHeading(stringResource(R.string.teams_current))
}

/** Shown when no character has been picked yet. */
@Composable
fun EmptySelection() {
    Text(
        text = stringResource(R.string.teams_empty_selection),
        style = MaterialTheme.typography.bodySmall,
        modifier = Modifier.padding(horizontal = 16.dp),
    )
}

/** One occupied party slot with a remove action. */
@Composable
fun SelectedTeamRow(
    character: Character,
    onRemove: () -> Unit,
) {
    ListItem(
        headlineContent = { Text(character.name) },
        supportingContent = {
            Text(
                stringResource(
                    R.string.element_role_pair,
                    stringResource(character.element.labelRes()),
                    stringResource(character.role.labelRes()),
                ),
            )
        },
        trailingContent = {
            IconButton(onClick = onRemove) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = stringResource(R.string.teams_remove),
                )
            }
        },
    )
}

/** Reason the current selection cannot be saved. */
@Composable
fun ValidationError(reason: String) {
    Text(
        text = reason,
        color = MaterialTheme.colorScheme.error,
        style = MaterialTheme.typography.bodySmall,
        modifier = Modifier.padding(horizontal = 16.dp),
    )
}

/** Actionable notes about the current party composition. */
@Composable
fun SynergyNotes(insights: List<SynergyInsight>) {
    if (insights.isEmpty()) return

    SectionHeading(stringResource(R.string.teams_synergy))

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

/** Heading above the roster picker. */
@Composable
fun RosterHeader() {
    SectionHeading(stringResource(R.string.teams_pick_character))
}

/** One roster entry with a selection checkbox. */
@Composable
fun RosterRow(
    character: Character,
    isSelected: Boolean,
    onToggle: () -> Unit,
) {
    ListItem(
        headlineContent = { Text(character.name) },
        supportingContent = {
            Text(
                stringResource(
                    R.string.element_role_pair,
                    stringResource(character.element.labelRes()),
                    stringResource(character.role.labelRes()),
                ),
            )
        },
        leadingContent = {
            Checkbox(
                checked = isSelected,
                onCheckedChange = { onToggle() },
            )
        },
    )
}

@Composable
private fun SectionHeading(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleSmall,
        modifier = Modifier.padding(start = 16.dp, top = 12.dp),
    )
}
