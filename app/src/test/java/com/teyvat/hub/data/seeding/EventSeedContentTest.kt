package com.teyvat.hub.data.seeding

import com.teyvat.hub.domain.model.EventStatus
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Verifies the seeded event windows cover every lifecycle state.
 *
 * A hard-coded anchor silently pushed every row into the "ended" section once
 * the anchor fell behind the wall clock, which left the calendar with no
 * running or upcoming content. These assertions pin the intended mix.
 */
class EventSeedContentTest {

    private val events = EventSeedContent.all(EventSeedContent.REFERENCE_NOW)

    @Test
    fun `seed produces a running event`() {
        assertTrue(
            events.any { it.startsAt <= now && (it.endsAt == null || it.endsAt > now) },
        )
    }

    @Test
    fun `seed produces an upcoming event`() {
        assertTrue(events.any { it.startsAt > now })
    }

    @Test
    fun `seed produces a finished event`() {
        assertTrue(events.any { it.endsAt != null && it.endsAt <= now })
    }

    @Test
    fun `every section of the calendar is populated`() {
        val statuses = events.map { statusAt(it.startsAt, it.endsAt) }

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
        val later = EventSeedContent.all(EventSeedContent.REFERENCE_NOW + 365L * 24L * 60L * 60L * 1000L)

        val statuses = later.map { statusAt(it.startsAt, it.endsAt) }
        assertTrue(statuses.contains(EventStatus.ACTIVE))
        assertTrue(statuses.contains(EventStatus.UPCOMING))
    }

    private val now = EventSeedContent.REFERENCE_NOW

    private fun statusAt(startsAt: Long, endsAt: Long?): EventStatus = when {
        now < startsAt -> EventStatus.UPCOMING
        endsAt != null && now >= endsAt -> EventStatus.ENDED
        else -> EventStatus.ACTIVE
    }
}
