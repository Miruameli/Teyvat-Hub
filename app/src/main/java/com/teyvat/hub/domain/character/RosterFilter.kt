package com.teyvat.hub.domain.character

/**
 * The narrowings a roster list can apply at once.
 *
 * Every field is optional: a null element means "any element", a blank query
 * means "no text filtering". Holding all of them in one value keeps the
 * combination rule in one pure function instead of spread across a screen.
 */
data class RosterFilter(
    val query: String = "",
    val element: Element? = null,
    val onlyFavorites: Boolean = false,
)

/**
 * Applies a filter to a roster.
 *
 * The rules are deliberately independent of order, so a search and a
 * favourite filter and an element filter can never disagree about which
 * characters belong on screen. An element filter that matches nothing
 * returns nothing rather than falling back to the whole list, because a
 * filter the player chose must never silently widen what they see.
 */
fun List<Character>.matching(filter: RosterFilter): List<Character> {
    val query = filter.query.trim()
    return filter { character ->
        val matchesText = query.isEmpty() || character.name.contains(query, ignoreCase = true)
        val matchesElement = filter.element == null || character.element == filter.element
        val matchesFavourites = !filter.onlyFavorites || character.isFavorite
        matchesText && matchesElement && matchesFavourites
    }
}
