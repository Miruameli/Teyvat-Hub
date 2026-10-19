package com.teyvat.hub.domain.model

/**
 * A Teyvat region with its associated element.
 */
data class Region(
    val key: String,
    val name: String,
    val element: String,
    val description: String
) {
    /** Returns the element key for this region. */
    fun getElementKey(): String = element
}