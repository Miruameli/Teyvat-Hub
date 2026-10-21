package com.teyvat.hub.presentation.feature.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
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
import com.teyvat.hub.BuildConfig
import com.teyvat.hub.R
import com.teyvat.hub.application.language.AppLanguage

/**
 * Settings hub for language, data attribution, privacy, and the disclaimer.
 *
 * Language is the only adjustable entry. The other entries are information:
 * the app stores nothing on a server, so there is no account, no sync, and
 * no toggle that would send data away.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = { TopAppBar(title = { Text(stringResource(R.string.settings_title)) }) },
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding)) {
            ListItem(
                headlineContent = { Text(stringResource(R.string.settings_language)) },
            )
            LanguageChoices(
                selected = state.language,
                onSelect = viewModel::onLanguageSelected,
            )
            HorizontalDivider()

            ListItem(
                headlineContent = { Text(stringResource(R.string.settings_version)) },
                supportingContent = { Text(BuildConfig.VERSION_NAME) },
            )
            HorizontalDivider()

            ListItem(
                headlineContent = { Text(stringResource(R.string.settings_data_source)) },
                supportingContent = {
                    Text(stringResource(R.string.settings_data_source_value))
                },
            )
            HorizontalDivider()

            ListItem(
                headlineContent = { Text(stringResource(R.string.settings_privacy)) },
                supportingContent = {
                    Text(stringResource(R.string.settings_privacy_value))
                },
            )
            HorizontalDivider()

            Text(
                text = stringResource(R.string.settings_disclaimer),
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(16.dp),
            )
        }
    }
}

/** The two languages the app ships with, as a pair of chips. */
@Composable
private fun LanguageChoices(
    selected: AppLanguage,
    onSelect: (AppLanguage) -> Unit,
) {
    Column(modifier = Modifier.padding(start = 16.dp, bottom = 12.dp)) {
        AppLanguage.entries.forEach { language ->
            FilterChip(
                selected = language == selected,
                onClick = { onSelect(language) },
                label = { Text(stringResource(language.labelRes())) },
                modifier = Modifier.padding(vertical = 4.dp),
            )
        }
    }
}
