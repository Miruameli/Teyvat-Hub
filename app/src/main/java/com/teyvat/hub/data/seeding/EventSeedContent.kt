package com.teyvat.hub.data.seeding

import com.teyvat.hub.data.local.event.GameEventEntity

/**
 * Seed entries for events and wish banners.
 *
 * Windows are relative to the moment the app seeds the database rather than
 * fixed to an absolute date: a hard-coded anchor goes stale within weeks and
 * leaves the calendar with every row in the "ended" section. Offsets keep one
 * row in each lifecycle state for the whole life of the install.
 *
 * [REFERENCE_NOW] is the instant the layout is defined against, so tests can
 * assert the expected mix without depending on the wall clock.
 */
object EventSeedContent {

    private const val HOUR = 60L * 60L * 1000L
    private const val DAY = 24L * HOUR

    /** Reference instant for the seed layout: 2026-10-18T04:00:00Z. */
    const val REFERENCE_NOW = 1_792_296_000_000L

    /**
     * Seed rows anchored on [now].
     *
     * Two rows are running, one is scheduled ahead, and one has already
     * finished, so the calendar opens with content in every section.
     */
    fun all(now: Long): List<GameEventEntity> = listOf(
        event(
            id = "event-firefly-flower",
            title = "Bunga Kunang-Kunang",
            kindKey = "event",
            startsAt = now - (2 * DAY),
            endsAt = now + (10 * DAY),
            description = "Event eksplorasi dengan haduharat resin dan materi talenta.",
        ),
        event(
            id = "banner-yae-miko",
            title = "Banner Yae Miko",
            kindKey = "banner",
            startsAt = now - (2 * DAY),
            endsAt = now + (6 * DAY),
            description = "Wish banner karakter 5 bintang dengan counter pity terpisah.",
        ),
        event(
            id = "banner-kagura",
            title = "Banner senjata Kagura's Verity",
            kindKey = "banner",
            startsAt = now + (7 * DAY),
            endsAt = now + (20 * DAY),
            description = "Wish banner senjata dengan hadiah epitomized path.",
        ),
        event(
            id = "event-music-fest",
            title = "Festival Musik Iridescent",
            kindKey = "event",
            startsAt = now - (30 * DAY),
            endsAt = now - (16 * DAY),
            description = "Event mini-game menari dengan hadiah Primogem.",
        ),
    )

    private fun event(
        id: String,
        title: String,
        kindKey: String,
        startsAt: Long,
        endsAt: Long,
        description: String,
    ) = GameEventEntity(
        id = id,
        title = title,
        kindKey = kindKey,
        startsAt = startsAt,
        endsAt = endsAt,
        description = description,
    )
}
