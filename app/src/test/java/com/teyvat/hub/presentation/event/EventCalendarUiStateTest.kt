package com.teyvat.hub.presentation.event

import com.teyvat.hub.domain.event.EventKind
import com.teyvat.hub.domain.event.EventStatus
import com.teyvat.hub.domain.event.GameEvent
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/** Exercises lifecycle splitting and filter rules that drive the calendar. */
class EventCalendarUiStateTest {

    private val now = 1_000_000L

    private val active = event("active", startsAt = 0, endsAt = 2_000_000L)
    private val upcoming = event("upcoming", startsAt = 1_500_000L, endsAt = null)
    private val ended = event("ended", startsAt = 0, endsAt = 500_000L)

    /** Fixed labels so assertions never depend on host locale or clock. */
    private val formatter = object : EventDateFormatter {
        override fun range(startsAt: Long, endsAt: Long?): String = "range"
    }

    @Test
    fun `rows split by lifecycle status`() {
        val state = buildEventCalendarState(
            events = listOf(active, upcoming, ended),
            now = now,
            formatter = formatter,
        )

        assertEquals(listOf("active"), state.active.map { it.id })
        assertEquals(listOf("upcoming"), state.upcoming.map { it.id })
        assertEquals(listOf("ended"), state.ended.map { it.id })
    }

    @Test
    fun `no end date keeps event active once started`() {
        val state = buildEventCalendarState(
            events = listOf(active.copy(endsAt = null)),
            now = now,
            formatter = formatter,
        )

        assertTrue(state.active.isNotEmpty())
        assertEquals(EventStatus.ACTIVE, state.active.single().status)
    }

    @Test
    fun `kind filter hides other kinds`() {
        val events = listOf(
            active.copy(id = "e1", kind = EventKind.EVENT),
            active.copy(id = "b1", kind = EventKind.BANNER),
        )

        val eventsOnly = buildEventCalendarState(
            events = events,
            now = now,
            kindFilter = EventKind.EVENT,
            formatter = formatter,
        )
        val bannersOnly = buildEventCalendarState(
            events = events,
            now = now,
            kindFilter = EventKind.BANNER,
            formatter = formatter,
        )

        assertEquals(listOf("e1"), eventsOnly.active.map { it.id })
        assertEquals(listOf("b1"), bannersOnly.active.map { it.id })
    }

    @Test
    fun `empty result flagged so screen shows empty message`() {
        val state = buildEventCalendarState(
            events = emptyList(),
            now = now,
            formatter = formatter,
        )

        assertTrue(state.isEmpty)
    }

    @Test
    fun `non-empty result clears the empty flag`() {
        val state = buildEventCalendarState(
            events = listOf(active),
            now = now,
            formatter = formatter,
        )

        assertFalse(state.isEmpty)
    }

    @Test
    fun `row carries display fields for rendering`() {
        val state = buildEventCalendarState(
            events = listOf(active.copy(description = "Desc")),
            now = now,
            formatter = formatter,
        )

        val row = state.active.single()
        assertEquals("Desc", row.description)
        assertEquals("range", row.rangeLabel)
        assertEquals("Event", row.kindLabel)
    }

    @Test
    fun `banner rows are labelled as banners`() {
        val state = buildEventCalendarState(
            events = listOf(active.copy(kind = EventKind.BANNER)),
            now = now,
            formatter = formatter,
        )

        assertEquals("Banner", state.active.single().kindLabel)
    }

    @Test
    fun `row keeps description so the screen can render it`() {
        val state = buildEventCalendarState(
            events = listOf(active.copy(description = "Hadiah resin")),
            now = now,
            formatter = formatter,
        )

        assertEquals("Hadiah resin", state.active.single().description)
    }

    private fun event(
        id: String,
        startsAt: Long,
        endsAt: Long?,
        kind: EventKind = EventKind.EVENT,
    ) = GameEvent(
        id = id,
        title = "Title $id",
        kind = kind,
        startsAt = startsAt,
        endsAt = endsAt,
        description = "",
    )
}
