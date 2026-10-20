package com.teyvat.hub.presentation.team

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.teyvat.hub.R
import com.teyvat.hub.domain.model.Character

/**
 * Scrollable roster picker for team slots.
 *
 * A character already in the team is shown as selected; the disabled state
 * keeps the list readable once the party is full instead of hiding rows.
 */
@Composable
fun CharacterPicker(
    roster: List<Character>,
    selected: List<Character>,
    onToggle: (Character) -> Unit,
) {
    val selectedIds = selected.map { it.id }.toSet()

    Text(
        text = stringResource(R.string.teams_pick_character),
        style = MaterialTheme.typography.titleSmall,
        modifier = Modifier.padding(start = 16.dp, top = 12.dp),
    )

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(roster, key = { it.id }) { character ->
            val isSelected = character.id in selectedIds
            ListItem(
                headlineContent = { Text(character.name) },
                supportingContent = {
                    Text("${character.element.key} · ${character.role.key}")
                },
                leadingContent = {
                    Checkbox(
                        checked = isSelected,
                        onCheckedChange = { onToggle(character) },
                    )
                },
            )
        }
    }
}
