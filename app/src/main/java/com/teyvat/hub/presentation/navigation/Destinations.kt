package com.teyvat.hub.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Top-level destinations shown in the bottom navigation bar.
 *
 * Routes are stable strings rather than generated ids so deep links and
 * navigation state survive process death.
 */
enum class Destination(
    val route: String,
    val labelResId: Int,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
) {
    ROSTER("roster", com.teyvat.hub.R.string.tab_roster, Icons.Filled.Person, Icons.Outlined.Person),
    CHECKLIST("checklist", com.teyvat.hub.R.string.tab_checklist, Icons.Filled.CheckCircle, Icons.Outlined.CheckCircle),
    TEAMS("teams", com.teyvat.hub.R.string.tab_teams, Icons.Filled.Favorite, Icons.Outlined.FavoriteBorder),
    EVENTS("events", com.teyvat.hub.R.string.tab_events, Icons.Filled.DateRange, Icons.Outlined.DateRange),
    SETTINGS("settings", com.teyvat.hub.R.string.tab_settings, Icons.Filled.Settings, Icons.Outlined.Settings),
}
