package com.teyvat.hub.data.mapper

import com.teyvat.hub.data.local.weapon.WeaponEntity
import com.teyvat.hub.domain.equipment.Weapon

/**
 * Converts stored weapon rows into the domain model.
 *
 * The key resolution comes from CharacterMapper, which already decides what an
 * unknown weapon type or rarity becomes, so there is one rule for both paths
 * into the domain rather than two that can drift apart.
 */
fun WeaponEntity.toDomain(): Weapon = Weapon(
    id = id,
    name = name,
    type = typeKey.toWeaponType(),
    rarity = rarityStars.toRarity(),
    baseAttack = baseAttack,
    subStat = subStat,
    subStatValue = subStatValue,
    bestFor = emptyList(),
    passive = passive,
)