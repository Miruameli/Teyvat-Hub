package com.teyvat.hub.presentation.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.teyvat.hub.BuildConfig
import com.teyvat.hub.R

/**
 * Settings hub for language, data attribution, privacy, and the disclaimer.
 *
 * The screen is read-only by design: Teyvat Hub stores nothing on a server,
 * so there is no account, no sync, and no toggle that would send data away.
 * Only the language entry is actionable, and it points to the system setting
 * because the app ships a single translation for now.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen() {
    Scaffold(
        topBar = { TopAppBar(title = { Text(stringResource(R.string.settings_title)) }) },
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding)) {
            ListItem(
                headlineContent = { Text(stringResource(R.string.settings_language)) },
                supportingContent = {
                    Text(stringResource(R.string.settings_language_value))
                },
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
