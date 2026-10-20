package com.teyvat.hub.presentation.event

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
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
import com.teyvat.hub.domain.model.EventKind

/**
 * Event and wish banner calendar.
 *
 * Sections are driven by lifecycle status resolved in the ViewModel; a filter
 * change recomputes rows from state that is already in memory, so no extra
 * database read happens while switching between all, events, and banners.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventCalendarScreen(
    viewModel: EventCalendarViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = { TopAppBar(title = { Text(stringResource(R.string.events_title)) }) },
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding)) {
            FilterChip(
                selected = state.kindFilter != null,
                onClick = viewModel::onCycleKindFilter,
                label = { Text(stringResource(kindFilterLabelRes(state))) },
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            )

            if (state.isEmpty) {
                Text(
                    text = stringResource(R.string.events_empty),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(16.dp),
                )
                return@Scaffold
            }

            LazyColumn(modifier = Modifier.fillMaxSize()) {
                if (state.active.isNotEmpty()) {
                    item { SectionTitle(stringResource(R.string.events_active)) }
                    items(state.active, key = { it.id }) { row -> EventRowCard(row) }
                }
                if (state.upcoming.isNotEmpty()) {
                    item { SectionTitle(stringResource(R.string.events_upcoming)) }
                    items(state.upcoming, key = { it.id }) { row -> EventRowCard(row) }
                }
                items(state.ended, key = { it.id }) { row -> EventRowCard(row) }
            }
        }
    }
}

@Composable
private fun EventRowCard(row: EventRow) {
    ListItem(
        headlineContent = { Text(row.title) },
        supportingContent = {
            Text("${row.kindLabel} · ${row.rangeLabel}")
        },
    )
}

@Composable
private fun SectionTitle(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleSmall,
        modifier = Modifier.padding(start = 16.dp, top = 12.dp),
    )
}

private fun kindFilterLabelRes(state: EventCalendarUiState): Int = when (state.kindFilter) {
    EventKind.EVENT -> R.string.filter_events_only
    EventKind.BANNER -> R.string.filter_banners_only
    null -> R.string.filter_all
}
