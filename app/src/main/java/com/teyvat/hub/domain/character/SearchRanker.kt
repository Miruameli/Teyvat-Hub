package com.teyvat.hub.domain.character

/** A single search result with its relevance score. */
data class SearchHit<T>(val item: T, val score: Int)

/**
 * Scores character names against a query.
 *
 * Ranking is deliberately simple and deterministic: exact match first, then
 * prefix match, then substring anywhere, then fuzzy prefix on each token.
 * Fuzzy matching uses a plain edit-distance ratio so it needs no library and
 * produces the same ordering on every run.
 *
 * The fuzzy step reads the words of a name without splitting it. Splitting
 * would allocate an array and a string for every word of every candidate, and
 * the search box re-ranks on every keystroke.
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

        val hits = ArrayList<SearchHit<T>>()
        for (candidate in candidates) {
            val score = score(nameSelector(candidate), normalizedQuery)
            if (score >= minScore) hits.add(SearchHit(candidate, score))
        }
        hits.sortByDescending { it.score }
        return if (hits.size <= limit) hits else hits.subList(0, limit).toList()
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

    /**
     * True when the query is a prefix of any word in the name, or a word in
     * the name is a prefix of the query.
     *
     * The words are read in place, so nothing is allocated for a name that
     * does not match.
     */
    private fun isFuzzyPrefix(query: String, name: String): Boolean {
        if (query.isEmpty()) return false
        var wordStart = 0
        while (wordStart < name.length) {
            var wordEnd = wordStart
            while (wordEnd < name.length && !isBoundary(name[wordEnd])) {
                wordEnd += 1
            }
            if (wordMatches(query, name, wordStart, wordEnd)) return true
            wordStart = wordEnd + 1
        }
        return false
    }

    /** Compares the query with the word between [start] and [end]. */
    private fun wordMatches(query: String, name: String, start: Int, end: Int): Boolean {
        val length = end - start
        if (length == 0) return false
        val shorter = minOf(length, query.length)
        for (offset in 0 until shorter) {
            if (name[start + offset] != query[offset]) return false
        }
        return length <= query.length
    }

    /** True for the characters that end a word. */
    private fun isBoundary(character: Char): Boolean =
        character == ' ' || character == '-' || character == '\''

    private companion object {
        const val EXACT = 4
        const val PREFIX = 3
        const val SUBSTRING = 2
        const val FUZZY = 1
        const val NO_MATCH = 0
    }
}
