package com.teyvat.hub.domain.model

/**
 * An element with its visual properties.
 */
data class Element(
    val key: String,
    val name: String,
    val color: String,
    val icon: String,
    val reaction: String
) {
    /** Returns a Compose Color from the hex string. */
    fun toColor(): androidx.compose.ui.graphics.Color {
        return androidx.compose.ui.graphics.Color(Color.parseColor(color))
    }
}