package com.teyvat.hub.application.repository

import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import com.teyvat.hub.data.local.character.CharacterDao
import com.teyvat.hub.data.mapper.toDomain
import com.teyvat.hub.domain.character.Character
import com.teyvat.hub.domain.character.SearchRanker

/**
 * Single source of truth for the character roster.
 *
 * The screen depends on this abstraction, not Room directly, so the storage
 * engine can be replaced without touching presentation code.
 */
interface CharacterRepository {
    fun observeRoster(): Flow<List<Character>>
    fun observeFavorites(): Flow<List<Character>>
    fun search(query: String): Flow<List<Character>>
    suspend fun toggleFavorite(id: String)
}

@Singleton
class LocalCharacterRepository @Inject constructor(
    private val dao: CharacterDao,
) : CharacterRepository {

    // Owned here rather than injected: a generic type cannot be bound by Hilt
    // and the ranker has no state, so construction stays trivial to test.
    private val ranker = SearchRanker<Character> { it.name }

    override fun observeRoster(): Flow<List<Character>> =
        dao.observeAll().map { rows -> rows.map { it.toDomain() } }

    override fun observeFavorites(): Flow<List<Character>> =
        dao.observeFavorites().map { rows -> rows.map { it.toDomain() } }

    override fun search(query: String): Flow<List<Character>> =
        dao.observeAll().map { rows ->
            ranker.rank(query, rows.map { it.toDomain() }).map { it.item }
        }

    override suspend fun toggleFavorite(id: String) {
        val current = dao.findById(id) ?: return
        dao.setFavorite(id, !current.isFavorite)
    }
}
