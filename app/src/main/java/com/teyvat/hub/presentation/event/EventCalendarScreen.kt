package com.teyvat.hub.presentation.event

import androidx.compose.runtime.Composable
import com.teyvat.hub.presentation.navigation.Destination
import com.teyvat.hub.presentation.navigation.PlaceholderScreen

/**
 * Event and banner calendar.
 *
 * Placeholder while event rows are wired from Room; the destination exists so
 * the tab navigates and the app stays usable.
 */
@Composable
fun EventCalendarScreen() {
    PlaceholderScreen(title = Destination.EVENTS.route)
}
