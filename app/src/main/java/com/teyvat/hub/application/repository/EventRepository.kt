package com.teyvat.hub.application.repository

import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import com.teyvat.hub.data.local.event.GameEventDao
import com.teyvat.hub.data.mapper.toDomain
import com.teyvat.hub.domain.event.EventKind
import com.teyvat.hub.domain.event.GameEvent

/**
 * Single source of truth for events and wish banners.
 *
 * Every read derives lifecycle status from the row timestamps and the caller's
 * clock, so no event status is ever persisted and stale "active" rows disappear
 * once they end without a background job.
 */
interface EventRepository {
    fun observeAll(): Flow<List<GameEvent>>
    fun observeByKind(kind: EventKind): Flow<List<GameEvent>>
}

@Singleton
class LocalEventRepository @Inject constructor(
    private val dao: GameEventDao,
) : EventRepository {

    override fun observeAll(): Flow<List<GameEvent>> =
        dao.observeAll().map { rows -> rows.map { it.toDomain() } }

    override fun observeByKind(kind: EventKind): Flow<List<GameEvent>> =
        dao.observeByKind(kind.key).map { rows -> rows.map { it.toDomain() } }
}
