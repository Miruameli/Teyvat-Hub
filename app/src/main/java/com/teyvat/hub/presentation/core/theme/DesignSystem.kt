package com.teyvat.hub.presentation.core.theme

import androidx.compose.ui.graphics.Color
import com.teyvat.hub.domain.character.Element

/**
 * The colour palette of Teyvat Hub.
 *
 * The dark blue is the night sky of Teyvat and the teal is the Anemo element.
 * Both come from the launcher emblem, so the icon and the app share one
 * identity instead of showing two unrelated brands.
 */

/** Night sky, used as the dark surface and as the launcher background. */
val TeyvatNight = Color(0xFF16233F)

/** A lighter step of the night sky, for raised surfaces in the dark theme. */
val TeyvatNightRaised = Color(0xFF1E2E4F)

/** The deepest step, for the background behind cards. */
val TeyvatNightDeep = Color(0xFF0E1526)

/** Anemo teal, the primary accent and the launcher emblem colour. */
val TeyvatTeal = Color(0xFF35D6C3)

/** A deeper teal for pressed states and for text on a light background. */
val TeyvatTealDeep = Color(0xFF0E8C7E)

/** The pale teal used for the emblem on the launcher icon. */
val TeyvatTealPale = Color(0xFF7FD4C1)

/** Warm gold, used for the five star rarity. */
val TeyvatGold = Color(0xFFE8C36A)

/** The light background of the app in the light theme. */
val TeyvatDaylight = Color(0xFFF6F7FB)

/** The light surface of cards in the light theme. */
val TeyvatDaylightRaised = Color(0xFFFFFFFF)

/**
 * The colour of every element, used as an avatar fallback when the network
 * cannot deliver a portrait.
 *
 * Element identity is carried by colour in the game itself, so a player who
 * is offline can still read a row by its colour even without the image.
 */
fun Element.colorSwatch(): Color = when (this) {
    Element.ANEMO -> Color(0xFF74C2A8)
    Element.GEO -> Color(0xFFD2A155)
    Element.ELECTRO -> Color(0xFF9A7BD1)
    Element.DENDRO -> Color(0xFF8FBF5A)
    Element.HYDRO -> Color(0xFF4FA5E0)
    Element.PYRO -> Color(0xFFE0644B)
    Element.CRYO -> Color(0xFF6FC9DE)
    Element.PHYSICAL -> Color(0xFF9BA3AF)
}
