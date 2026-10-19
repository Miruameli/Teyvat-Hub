package com.teyvat.hub.presentation.settings

import androidx.compose.runtime.Composable
import com.teyvat.hub.presentation.navigation.Destination
import com.teyvat.hub.presentation.navigation.PlaceholderScreen

/**
 * Settings including language, data attribution, and disclaimer.
 *
 * Placeholder while preferences are wired to DataStore; the destination exists
 * so the tab navigates and the app stays usable.
 */
@Composable
fun SettingsScreen() {
    PlaceholderScreen(title = Destination.SETTINGS.route)
}
