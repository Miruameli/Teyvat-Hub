package com.teyvat.hub.data.content.seeding

import com.teyvat.hub.data.local.checklist.ChecklistItemEntity

/**
 * Default farming tasks seeded into the database on first launch.
 *
 * Ids are stable strings so future content updates can replace rows without
 * losing a player's completion history.
 */
object ChecklistSeedContent {

    fun daily(): List<ChecklistItemEntity> = listOf(
        task("daily-commissions", "Komisi harian (4)", ResetKey.DAILY),
        task("daily-resin", "Gunakan resin", ResetKey.DAILY),
        task("daily-domain", "Clear domain (1x)", ResetKey.DAILY),
        task("daily-boss", "Boss mingguan harian", ResetKey.DAILY),
        task("daily-ore", "Tambang kristal (3x)", ResetKey.DAILY),
    )

    fun weekly(): List<ChecklistItemEntity> = listOf(
        task("weekly-boss", "Boss mingguan", ResetKey.WEEKLY),
        task("weekly-trounce", "Trounce domain", ResetKey.WEEKLY),
        task("weekly-reputation", "Quest reputasi kota", ResetKey.WEEKLY),
        task("weekly-expedition", "Kirim ekspedisi", ResetKey.WEEKLY),
        task("weekly-teapot", "Hadiah Seru Teh", ResetKey.WEEKLY),
    )

    fun all(): List<ChecklistItemEntity> = daily() + weekly()

    private fun task(id: String, title: String, cadence: String) = ChecklistItemEntity(
        id = id,
        title = title,
        cadenceKey = cadence,
        lastCompletedAt = null,
        isCustom = false,
    )

    private object ResetKey {
        const val DAILY = "daily"
        const val WEEKLY = "weekly"
    }
}
