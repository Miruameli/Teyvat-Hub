package com.teyvat.hub.data.mapper

import com.teyvat.hub.data.local.weapon.WeaponEntity
import com.teyvat.hub.domain.character.WeaponType
import com.teyvat.hub.domain.equipment.Weapon
import com.teyvat.hub.domain.character.Rarity

/**
 * Converts stored weapon rows into the domain model.
 *
 * An unknown type key falls back to the sword rather than throwing: the row
 * still carries a name, a rarity, and numbers the player can read, so the
 * only loss is which icon the family shows.
 */
fun WeaponEntity.toDomain(): Weapon = Weapon(
    id = id,
    name = name,
    type = typeKey.toWeaponTypeKey(),
    rarity = rarityStars.toRarity(),
    baseAttack = baseAttack,
    subStat = subStat,
    subStatValue = subStatValue,
    bestFor = emptyList(),
    passive = passive,
)

private fun String.toWeaponTypeKey(): WeaponType =
    WeaponType.entries.firstOrNull { it.key == this } ?: WeaponType.SWORD

private fun Int.toRarity(): Rarity =
    if (this >= 5) Rarity.FIVE else Rarity.FOUR