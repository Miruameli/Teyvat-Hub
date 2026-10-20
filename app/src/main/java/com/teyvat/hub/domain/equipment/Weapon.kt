package com.teyvat.hub.domain.equipment

import com.teyvat.hub.domain.character.Rarity
import com.teyvat.hub.domain.character.WeaponType

/**
 * A weapon entry from the database.
 *
 * [bestFor] names the characters most often recommended with this weapon so the
 * detail screen can cross-link to character builds without a second lookup.
 */
data class Weapon(
    val id: String,
    val name: String,
    val type: WeaponType,
    val rarity: Rarity,
    val baseAttack: Int,
    val subStat: String,
    val subStatValue: String,
    val bestFor: List<String> = emptyList(),
    val passive: String = "",
)
