package com.teyvat.hub.data.seeding

import com.teyvat.hub.data.mapper.toDomain
import com.teyvat.hub.domain.event.EventStatus
import com.teyvat.hub.domain.event.GameEvent
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Verifies the seeded event windows cover every lifecycle state.
 *
 * A hard-coded anchor silently pushed every row into the "ended" section once
 * the anchor fell behind the wall clock, which left the calendar with no
 * running or upcoming content. These assertions pin the intended mix.
 *
 * Statuses are resolved through the real domain model rather than a local
 * copy of the rule, so the test fails if the production logic drifts.
 */
class EventSeedContentTest {

    private val referenceNow = EventSeedContent.REFERENCE_NOW

    private val events: List<GameEvent> =
        EventSeedContent.all(referenceNow).map { it.toDomain() }

    @Test
    fun `seed produces a running event`() {
        assertTrue(events.any { it.statusAt(referenceNow) == EventStatus.ACTIVE })
    }

    @Test
    fun `seed produces an upcoming event`() {
        assertTrue(events.any { it.statusAt(referenceNow) == EventStatus.UPCOMING })
    }

    @Test
    fun `seed produces a finished event`() {
        assertTrue(events.any { it.statusAt(referenceNow) == EventStatus.ENDED })
    }

    @Test
    fun `every section of the calendar is populated`() {
        val statuses = events.map { it.statusAt(referenceNow) }

        assertTrue(statuses.contains(EventStatus.ACTIVE))
        assertTrue(statuses.contains(EventStatus.UPCOMING))
        assertTrue(statuses.contains(EventStatus.ENDED))
    }

    @Test
    fun `seed ids are stable so refresh overwrites instead of duplicating`() {
        val ids = events.map { it.id }

        assertEquals(ids.size, ids.toSet().size)
        assertEquals(4, ids.size)
    }

    @Test
    fun `windows stay inside the announced range for any seed moment`() {
        val laterNow = referenceNow + 365L * 24L * 60L * 60L * 1000L
        val later = EventSeedContent.all(laterNow).map { it.toDomain() }

        val statuses = later.map { it.statusAt(laterNow) }
        assertTrue(statuses.contains(EventStatus.ACTIVE))
        assertTrue(statuses.contains(EventStatus.UPCOMING))
    }
}
