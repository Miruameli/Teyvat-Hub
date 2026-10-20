package com.teyvat.hub.application

import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import com.teyvat.hub.data.local.team.TeamDao
import com.teyvat.hub.data.mapper.toDomain
import com.teyvat.hub.data.mapper.toEntity
import com.teyvat.hub.domain.team.Team

/**
 * Single source of truth for saved teams and built-in templates.
 *
 * Validation lives in the domain layer, so this repository only persists
 * teams that already passed [com.teyvat.hub.domain.team.TeamValidator].
 */
interface TeamRepository {
    fun observeSaved(): Flow<List<Team>>
    fun observeTemplates(): Flow<List<Team>>
    suspend fun save(team: Team)
    suspend fun delete(id: String)
}

@Singleton
class LocalTeamRepository @Inject constructor(
    private val dao: TeamDao,
) : TeamRepository {

    override fun observeSaved(): Flow<List<Team>> =
        dao.observeSaved().map { rows -> rows.map { it.toDomain() } }

    override fun observeTemplates(): Flow<List<Team>> =
        dao.observeTemplates().map { rows -> rows.map { it.toDomain() } }

    override suspend fun save(team: Team) {
        dao.upsertAll(listOf(team.toEntity()))
    }

    override suspend fun delete(id: String) {
        dao.deleteById(id)
    }
}
