package com.teyvat.hub.data.seeding

import com.teyvat.hub.data.local.event.GameEventEntity

/**
 * Seed entries for events and wish banners.
 *
 * Timestamps are epoch millis computed from a fixed anchor so seeded data is
 * deterministic in tests. Live dates are refreshed through content updates.
 */
object EventSeedContent {

    private const val HOUR = 60L * 60L * 1000L
    private const val DAY = 24L * HOUR

    /** Fixed anchor: 2026-10-18T04:00:00Z, a server reset moment. */
    private const val ANCHOR = 1_790_562_400_000L

    fun all(): List<GameEventEntity> = listOf(
        event(
            id = "event-firefly-flower",
            title = "Bunga Kunang-Kunang",
            kindKey = "event",
            startsAt = ANCHOR - (2 * DAY),
            endsAt = ANCHOR + (10 * DAY),
            description = "Event eksplorasi dengan haduharat resin dan materi talenta.",
        ),
        event(
            id = "banner-yae-miko",
            title = "Banner Yae Miko",
            kindKey = "banner",
            startsAt = ANCHOR - (2 * DAY),
            endsAt = ANCHOR + (6 * DAY),
            description = "Wish banner karakter 5 bintang dengan counter pity terpisah.",
        ),
        event(
            id = "banner-kagura",
            title = "Banner senjata Kagura's Verity",
            kindKey = "banner",
            startsAt = ANCHOR + (7 * DAY),
            endsAt = ANCHOR + (20 * DAY),
            description = "Wish banner senjata dengan hadiah epitomized path.",
        ),
        event(
            id = "event-music-fest",
            title = "Festival Musik Iridescent",
            kindKey = "event",
            startsAt = ANCHOR - (30 * DAY),
            endsAt = ANCHOR - (16 * DAY),
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
