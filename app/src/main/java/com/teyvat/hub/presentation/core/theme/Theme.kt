package com.teyvat.hub.presentation.core.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

/** The colour scheme of the app in the light theme. */
private val LightColors = lightColorScheme(
    primary = TeyvatTealDeep,
    onPrimary = TeyvatDaylightRaised,
    primaryContainer = TeyvatTealPale,
    onPrimaryContainer = TeyvatNightDeep,
    secondary = TeyvatNight,
    onSecondary = TeyvatDaylightRaised,
    secondaryContainer = TeyvatDaylightRaised,
    onSecondaryContainer = TeyvatNight,
    tertiary = TeyvatGold,
    onTertiary = TeyvatNightDeep,
    background = TeyvatDaylight,
    onBackground = TeyvatNight,
    surface = TeyvatDaylightRaised,
    onSurface = TeyvatNight,
    surfaceVariant = TeyvatDaylight,
    onSurfaceVariant = TeyvatNightRaised,
    outline = TeyvatTealDeep,
)

/** The colour scheme of the app in the dark theme. */
private val DarkColors = darkColorScheme(
    primary = TeyvatTeal,
    onPrimary = TeyvatNightDeep,
    primaryContainer = TeyvatTealDeep,
    onPrimaryContainer = TeyvatTealPale,
    secondary = TeyvatTealPale,
    onSecondary = TeyvatNightDeep,
    secondaryContainer = TeyvatNightRaised,
    onSecondaryContainer = TeyvatTealPale,
    tertiary = TeyvatGold,
    onTertiary = TeyvatNightDeep,
    background = TeyvatNightDeep,
    onBackground = TeyvatTealPale,
    surface = TeyvatNight,
    onSurface = TeyvatTealPale,
    surfaceVariant = TeyvatNightRaised,
    onSurfaceVariant = TeyvatTealPale,
    outline = TeyvatTeal,
)

/**
 * Root theme of the application.
 *
 * Follows the system dark or light setting so that night-time farming
 * sessions do not flash a bright screen, which matters for a game companion
 * app. The colours, the type scale, and the corner radii all come from this
 * folder, so a screen never picks its own.
 */
@Composable
fun TeyvatHubTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        typography = TeyvatTypography,
        shapes = TeyvatShapes,
        content = content,
    )
}
