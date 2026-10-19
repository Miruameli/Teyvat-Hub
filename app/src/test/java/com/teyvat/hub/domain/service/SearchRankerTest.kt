package com.teyvat.hub.domain.service

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/** Verifies ranking order and edge cases of the search service. */
class SearchRankerTest {

    private val ranker = SearchRanker { it }

    @Test
    fun `empty query returns no results`() {
        val result = ranker.rank("   ", listOf("Albedo", "Kazuha"))

        assertTrue(result.isEmpty())
    }

    @Test
    fun `exact match ranks above prefix match`() {
        val result = ranker.rank("Albedo", listOf("Albedo", "Albedo Prime"))

        assertEquals("Albedo", result.first().item)
        assertEquals(2, result.size)
    }

    @Test
    fun `prefix match ranks above substring match`() {
        val result = ranker.rank("kaz", listOf("Raiden Kazuha", "Kazuha"))

        assertEquals("Kazuha", result.first().item)
    }

    @Test
    fun `matching is case insensitive`() {
        val result = ranker.rank("ALBEDO", listOf("albedo", "kazuha"))

        assertEquals("albedo", result.single().item)
    }

    @Test
    fun `non matching candidates are excluded`() {
        val result = ranker.rank("zzz", listOf("Albedo", "Kazuha"))

        assertTrue(result.isEmpty())
    }

    @Test
    fun `respects the limit`() {
        val many = (1..30).map { "Albedo $it" }

        val result = ranker.rank("Albedo", many, limit = 5)

        assertEquals(5, result.size)
    }

    @Test
    fun `token prefix matches without being a prefix of the whole name`() {
        val result = ranker.rank("raiden", listOf("Raiden Shogun", "Shogun"))

        assertEquals("Raiden Shogun", result.first().item)
    }

    @Test
    fun `minScore filters lower quality matches`() {
        val candidates = listOf("Albedo", "Raiden Kazuha")

        val result = ranker.rank("kaz", candidates, minScore = SUBSTRING_QUALITY)

        // Only the substring/prefix match survives; pure token matches are cut.
        assertEquals(listOf("Raiden Kazuha"), result.map { it.item })
    }

    private companion object {
        const val SUBSTRING_QUALITY = 2
    }
}
