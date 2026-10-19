package com.teyvat.hub.presentation.team

import androidx.compose.runtime.Composable
import com.teyvat.hub.presentation.navigation.Destination
import com.teyvat.hub.presentation.navigation.PlaceholderScreen

/**
 * Team builder for up to four characters.
 *
 * Placeholder while the builder is wired to saved teams; the destination exists
 * so the tab navigates and the app stays usable.
 */
@Composable
fun TeamBuilderScreen() {
    PlaceholderScreen(title = Destination.TEAMS.route)
}
