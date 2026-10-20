package com.teyvat.hub.presentation.event

import com.teyvat.hub.domain.event.EventKind
import com.teyvat.hub.domain.event.EventStatus
import com.teyvat.hub.domain.event.GameEvent

/**
 * Calendar state split by lifecycle plus the active kind filter.
 *
 * [rangeLabel] and [kindLabel] are pre-resolved UI strings so composables never
 * touch platform date formats; this keeps the state renderable in tests.
 */
data class EventCalendarUiState(
    val active: List<EventRow> = emptyList(),
    val upcoming: List<EventRow> = emptyList(),
    val ended: List<EventRow> = emptyList(),
    val kindFilter: EventKind? = null,
    val isEmpty: Boolean = true,
)

/** One event row with display-ready fields and its resolved lifecycle. */
data class EventRow(
    val id: String,
    val title: String,
    val description: String,
    val kindLabel: String,
    val rangeLabel: String,
    val status: EventStatus,
)

/** Builds the screen state from domain rows evaluated at [now]. */
fun buildEventCalendarState(
    events: List<GameEvent>,
    now: Long,
    kindFilter: EventKind? = null,
    formatter: EventDateFormatter = DefaultEventDateFormatter,
): EventCalendarUiState {
    val visible = events.filter { kindFilter == null || it.kind == kindFilter }
    val rows = visible.map { it.toRow(now, formatter) }
    return EventCalendarUiState(
        active = rows.filter { it.status == EventStatus.ACTIVE },
        upcoming = rows.filter { it.status == EventStatus.UPCOMING },
        ended = rows.filter { it.status == EventStatus.ENDED },
        kindFilter = kindFilter,
        isEmpty = rows.isEmpty(),
    )
}

private fun GameEvent.toRow(now: Long, formatter: EventDateFormatter): EventRow =
    EventRow(
        id = id,
        title = title,
        description = description,
        kindLabel = if (kind == EventKind.BANNER) BANNER_LABEL else EVENT_LABEL,
        rangeLabel = formatter.range(startsAt, endsAt),
        status = statusAt(now),
    )

private const val EVENT_LABEL = "Event"
private const val BANNER_LABEL = "Banner"
