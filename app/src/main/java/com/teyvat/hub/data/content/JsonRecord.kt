package com.teyvat.hub.data.content

/**
 * File-level shape of the bundled character asset.
 *
 * `imageSide` and `imageIcon` are kept so a caller can resolve art without
 * re-parsing the bundle; the roster mapper ignores them because Room has no
 * column for them yet, and an unknown-format row must fail to map loudly
 * rather than be silently stored as an empty name.
 */
data class CharacterRecord(
    val id: String,
    val name: String,
    val element: String,
    val weaponType: String,
    val rarity: Int,
    val region: String,
    val role: String,
    val imageSide: String? = null,
    val imageIcon: String? = null,
)

/** File-level shape of the bundled weapon asset. */
data class WeaponRecord(
    val id: String,
    val name: String,
    val type: String,
    val rarity: Int,
    val baseAttack: Int,
    val subStat: String,
    val subStatValue: String,
    val passive: String,
)

/**
 * File-level shape of the bundled region asset.
 *
 * Carries the display name, which is what the roster shows; the JSON matches
 * the lowercase key used by character rows, so joining the two is what turns
 * `mondstadt` back into `Mondstadt`.
 */
data class RegionRecord(
    val key: String,
    val name: String,
)