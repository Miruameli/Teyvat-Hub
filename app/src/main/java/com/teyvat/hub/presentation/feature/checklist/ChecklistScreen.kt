package com.teyvat.hub.presentation.feature.checklist

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.teyvat.hub.R
import com.teyvat.hub.domain.model.ChecklistItem

/**
 * Daily, weekly, and custom farming tasks.
 *
 * Checking a task records a completion timestamp; the pending state then
 * recomputes from the server reset window, so no background job or alarm is
 * needed for the list to refresh after 04:00 UTC+8.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChecklistScreen(
    viewModel: ChecklistViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    var customDraft by rememberSaveable { mutableStateOf("") }

    Scaffold(
        topBar = { TopAppBar(title = { Text(stringResource(R.string.checklist_title)) }) },
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding)) {
            TextField(
                value = customDraft,
                onValueChange = { customDraft = it },
                label = { Text(stringResource(R.string.action_add_task)) },
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                trailingIcon = {
                    IconButton(
                        onClick = {
                            viewModel.onAddCustomTask(customDraft)
                            customDraft = ""
                        },
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = stringResource(R.string.action_add_task),
                        )
                    }
                },
            )

            LazyColumn(modifier = Modifier.fillMaxSize()) {
                state.daily.forEach { item ->
                    item(key = "daily-${item.id}") {
                        ChecklistRow(item = item, onComplete = viewModel::onTaskCompleted)
                    }
                }
                state.weekly.forEach { item ->
                    item(key = "weekly-${item.id}") {
                        ChecklistRow(item = item, onComplete = viewModel::onTaskCompleted)
                    }
                }
                state.custom.forEach { item ->
                    item(key = "custom-${item.id}") {
                        ChecklistRow(
                            item = item,
                            onComplete = viewModel::onTaskCompleted,
                            onDelete = { viewModel.onDeleteTask(item) },
                        )
                    }
                }
            }
        }
    }
}

/** One farming task, with the delete action for the tasks the player added. */
@Composable
private fun ChecklistRow(
    item: ChecklistItem,
    onComplete: (ChecklistItem) -> Unit,
    onDelete: (() -> Unit)? = null,
) {
    ListItem(
        headlineContent = { Text(item.title) },
        leadingContent = {
            Checkbox(
                checked = false,
                onCheckedChange = { onComplete(item) },
            )
        },
        trailingContent = {
            if (onDelete != null) {
                IconButton(onClick = onDelete) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = stringResource(R.string.checklist_delete_task),
                    )
                }
            }
        },
    )
}
