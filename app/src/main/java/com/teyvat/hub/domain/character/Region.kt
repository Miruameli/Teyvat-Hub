package com.teyvat.hub.domain.character


/**
 * A nation/region in Teyvat with its associated element.
 *
 * Lives in [domain] so presentation code can render regions without
 * touching data layer, keeping business rules testable on the JVM.
 */
data class Region(
    val key: String,
    val name: String,
    val element: Element,
    val description: String
)