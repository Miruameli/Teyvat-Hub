package com.teyvat.hub.presentation.roster

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.teyvat.hub.R
import com.teyvat.hub.data.remote.EnkaAsset
import com.teyvat.hub.domain.model.Character

/**
 * Character roster list with search and favorite filtering.
 *
 * Rows use the 12.7 KB side avatar so a long list cannot exhaust the device
 * bitmap heap; see docs/adr/0003-optimized-genshin-digital-assets.md.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RosterScreen(
    viewModel: RosterViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = { TopAppBar(title = { Text(stringResource(R.string.roster_title)) }) },
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding)) {
            TextField(
                value = state.query,
                onValueChange = viewModel::onQueryChange,
                label = { Text(stringResource(R.string.action_search)) },
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            )
            Row(modifier = Modifier.padding(16.dp)) {
                FilterChip(
                    selected = state.onlyFavorites,
                    onClick = {
                        viewModel.onToggleFavoritesOnly(!state.onlyFavorites)
                    },
                    label = { Text(stringResource(R.string.filter_favorites)) },
                )
            }
            if (state.characters.isEmpty()) {
                Text(
                    text = stringResource(R.string.roster_empty),
                    modifier = Modifier.padding(16.dp),
                )
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(state.characters, key = { it.id }) { character ->
                        CharacterRow(
                            character = character,
                            onToggleFavorite = { viewModel.onToggleFavorite(character) },
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CharacterRow(
    character: Character,
    onToggleFavorite: () -> Unit,
) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 6.dp),
        onClick = onToggleFavorite,
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            AsyncImage(
                model = EnkaAsset.avatarSide(character.name),
                contentDescription = character.name,
                modifier = Modifier.size(56.dp),
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = character.name,
                    style = MaterialTheme.typography.titleMedium,
                )
                Text(
                    text = buildString {
                        append(character.element.key)
                        append(" - ")
                        append(character.region)
                    },
                    style = MaterialTheme.typography.bodySmall,
                )
            }
            Text(
                // LocalContext keeps the favorite star reading from resources so the
                // string can be translated without touching layout code.
                text = if (character.isFavorite) {
                    LocalContext.current.getString(R.string.marker_favorite_on)
                } else {
                    LocalContext.current.getString(R.string.marker_favorite_off)
                },
            )
        }
    }
}
