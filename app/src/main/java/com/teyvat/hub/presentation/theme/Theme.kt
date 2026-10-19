package com.teyvat.hub.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

/** Brand purple used for primary interactive elements. */
private val TeyvatPurple = Color(0xFF7B4DFF)

/** Deeper purple for surfaces in dark theme. */
private val TeyvatPurpleDark = Color(0xFF4A2BB0)

/** Accent teal highlighting elemental reactions and checklist progress. */
private val TeyvatTeal = Color(0xFF35D6C3)

private val LightColors = lightColorScheme(
    primary = TeyvatPurple,
    secondary = TeyvatTeal,
)

private val DarkColors = darkColorScheme(
    primary = TeyvatTeal,
    secondary = TeyvatPurple,
    surface = TeyvatPurpleDark,
)

/**
 * Root theme of the application.
 *
 * Follows the system dark/light setting so that night-time farming sessions do
 * not flash a bright screen, which matters for a game companion app.
 */
@Composable
fun TeyvatHubTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colorScheme = if (darkTheme) DarkColors else LightColors
    MaterialTheme(
        colorScheme = colorScheme,
        content = content,
    )
}
