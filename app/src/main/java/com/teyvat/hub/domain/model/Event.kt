package com.teyvat.hub.domain.model

/** Lifecycle state of an event or banner at a given moment. */
enum class EventStatus(val key: String) {
    ACTIVE("active"),
    UPCOMING("upcoming"),
    ENDED("ended"),
}

/**
 * An in-game event or wish banner window.
 *
 * [startsAt] and [endsAt] are epoch millis; [endsAt] may be null when the end
 * date is not announced yet.
 */
data class GameEvent(
    val id: String,
    val title: String,
    val kind: EventKind,
    val startsAt: Long,
    val endsAt: Long?,
    val description: String = "",
    val featuredCharacters: List<String> = emptyList(),
    val featuredWeapons: List<String> = emptyList(),
) {
    /**
     * Resolve lifecycle status at [now].
     *
     * An event with no announced end date stays [EventStatus.ACTIVE] once it has
     * started, because the server lets it run until further notice.
     */
    fun statusAt(now: Long): EventStatus = when {
        now < startsAt -> EventStatus.UPCOMING
        endsAt != null && now >= endsAt -> EventStatus.ENDED
        else -> EventStatus.ACTIVE
    }
}

/** Discriminates event rows so filters can separate them. */
enum class EventKind(val key: String) {
    EVENT("event"),
    BANNER("banner"),
}
