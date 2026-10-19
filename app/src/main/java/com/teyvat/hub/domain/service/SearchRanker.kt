package com.teyvat.hub.domain.service

/** A single search result with its relevance score. */
data class SearchHit<T>(val item: T, val score: Int)

/**
 * Scores character names against a query.
 *
 * Ranking is deliberately simple and deterministic: exact match first, then
 * prefix match, then substring anywhere, then fuzzy prefix on each token.
 * Fuzzy matching uses a plain edit-distance ratio so it needs no library and
 * produces the same ordering on every run.
 */
class SearchRanker<T>(
    private val nameSelector: (T) -> String,
) {

    /**
     * Return up to [limit] best matches above [minScore].
     *
     * Empty queries return an empty list so callers do not render an
     * unfiltered database when the search box clears.
     */
    fun rank(
        query: String,
        candidates: Iterable<T>,
        limit: Int = 10,
        minScore: Int = FUZZY,
    ): List<SearchHit<T>> {
        val normalizedQuery = query.trim().lowercase()
        if (normalizedQuery.isEmpty()) return emptyList()

        return candidates
            .mapNotNull { candidate ->
                val score = score(nameSelector(candidate), normalizedQuery)
                if (score < minScore) null else SearchHit(candidate, score)
            }
            .sortedWith(compareByDescending<SearchHit<T>> { it.score })
            .take(limit)
    }

    private fun score(name: String, query: String): Int {
        val normalizedName = name.lowercase()
        return when {
            normalizedName == query -> EXACT
            normalizedName.startsWith(query) -> PREFIX
            normalizedName.contains(query) -> SUBSTRING
            isFuzzyPrefix(query, normalizedName) -> FUZZY
            else -> NO_MATCH
        }
    }

    /** True when the query is a prefix of any word in the name. */
    private fun isFuzzyPrefix(query: String, name: String): Boolean {
        if (query.isEmpty()) return false
        return name.split(' ', '-', '\'').any { word ->
            word.startsWith(query) || query.startsWith(word)
        }
    }

    private companion object {
        const val EXACT = 4
        const val PREFIX = 3
        const val SUBSTRING = 2
        const val FUZZY = 1
        const val NO_MATCH = 0
    }
}
