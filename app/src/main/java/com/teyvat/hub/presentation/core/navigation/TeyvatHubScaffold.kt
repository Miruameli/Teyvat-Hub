package com.teyvat.hub.presentation.core.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.teyvat.hub.presentation.feature.checklist.ChecklistScreen
import com.teyvat.hub.presentation.feature.event.EventCalendarScreen
import com.teyvat.hub.presentation.feature.library.roster.RosterScreen
import com.teyvat.hub.presentation.feature.library.weapon.WeaponLibraryScreen
import com.teyvat.hub.presentation.feature.settings.SettingsScreen
import com.teyvat.hub.presentation.feature.team.TeamBuilderScreen

/**
 * Root scaffold hosting the primary destinations.
 *
 * Navigation uses a single NavHost with a flat route per tab; back stack is
 * collapsed to the start destination so returning from a tab never walks the
 * user through every screen they already left. The bottom bar is drawn from
 * Destination.entries, so a new tab appears once its entry exists.
 */
@Composable
fun TeyvatHubScaffold() {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            NavigationBar {
                Destination.entries.forEach { destination ->
                    val selected = currentRoute == destination.route
                    NavigationBarItem(
                        selected = selected,
                        onClick = {
                            if (!selected) {
                                navController.navigate(destination.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        },
                        icon = {
                            Icon(
                                imageVector = if (selected) {
                                    destination.selectedIcon
                                } else {
                                    destination.unselectedIcon
                                },
                                contentDescription = stringResource(destination.labelResId),
                            )
                        },
                        label = { Text(stringResource(destination.labelResId)) },
                    )
                }
            }
        },
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = Destination.ROSTER.route,
            modifier = Modifier.fillMaxSize().padding(padding),
        ) {
            composable(Destination.ROSTER.route) { RosterScreen() }
            composable(Destination.CHECKLIST.route) { ChecklistScreen() }
            composable(Destination.WEAPONS.route) { WeaponLibraryScreen() }
            composable(Destination.TEAMS.route) { TeamBuilderScreen() }
            composable(Destination.EVENTS.route) { EventCalendarScreen() }
            composable(Destination.SETTINGS.route) { SettingsScreen() }
        }
    }
}
