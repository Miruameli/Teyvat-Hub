package com.teyvat.hub.presentation.feature.library.weapon

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.teyvat.hub.R
import com.teyvat.hub.domain.character.WeaponType
import com.teyvat.hub.domain.equipment.Weapon
import com.teyvat.hub.presentation.core.theme.labelRes

/**
 * Weapon library showing every weapon the bundle ships.
 *
 * The passive description is what a player reads to decide whether a weapon
 * suits a character, so the row shows it rather than only the numbers.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeaponLibraryScreen(
    viewModel: WeaponLibraryViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = { TopAppBar(title = { Text(stringResource(R.string.weapons_title)) }) },
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding)) {
            // One chip per family, so narrowing the list is a single tap and
            // the active family is always visible.
            LazyRow(
                modifier = Modifier.padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(state.types, key = { it.name }) { type ->
                    FilterChip(
                        selected = state.typeFilter == type,
                        onClick = { viewModel.onTypeSelected(type) },
                        label = { Text(stringResource(type.labelRes())) },
                    )
                }
            }
            if (state.weapons.isEmpty()) {
                Text(
                    text = stringResource(R.string.weapons_empty),
                    modifier = Modifier.padding(16.dp),
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(
                        horizontal = 16.dp,
                        vertical = 8.dp,
                    ),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    items(state.weapons, key = { it.id }) { weapon ->
                        WeaponRow(weapon)
                    }
                }
            }
        }
    }
}

/** A single weapon: name, family, the numbers, and the passive description. */
@Composable
private fun WeaponRow(weapon: Weapon) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(text = weapon.name)
                Text(text = stringResource(weapon.type.labelRes()))
            }
            Text(
                text = stringResource(
                    R.string.weapon_summary_line,
                    weapon.baseAttack,
                    weapon.subStat,
                    weapon.subStatValue,
                ),
            )
            if (weapon.passive.isNotBlank()) {
                Text(text = weapon.passive)
            }
        }
    }
}