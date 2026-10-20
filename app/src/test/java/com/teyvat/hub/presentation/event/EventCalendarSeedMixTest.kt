package com.teyvat.hub.presentation.presentation.feature.event

import com.teyvat.hub.data.content.seeding.EventSeedContent
import com.teyvat.hub.data.mapper.toDomain
import org.junit.Assert.assertFalse
import org.junit.Test

/**
 * Verifies the calendar opens with content in every section.
 *
 * A stale seed anchor once pushed every row into the finished section, which
 * left the screen showing a single unlabelled list. This test guards the
 * end-to-end path from seeded rows to rendered sections.
 */
class EventCalendarSeedMixTest {

    @Test
    fun `seeded content fills every section of the calendar`() {
        val seeded = EventSeedContent.all(EventSeedContent.REFERENCE_NOW)
            .map { it.toDomain() }

        val state = buildEventCalendarState(
            events = seeded,
            now = EventSeedContent.REFERENCE_NOW,
            formatter = FixedFormatter,
        )

        assertFalse("active section must not be empty", state.active.isEmpty())
        assertFalse("upcoming section must not be empty", state.upcoming.isEmpty())
        assertFalse("ended section must not be empty", state.ended.isEmpty())
        assertFalse(state.isEmpty)
    }

    /** Fixed label so the assertion never depends on host locale data. */
    private object FixedFormatter : EventDateFormatter {
        override fun range(startsAt: Long, endsAt: Long?): String = "range"
    }
}
