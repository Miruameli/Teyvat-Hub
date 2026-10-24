package com.teyvat.hub.data.mapper

import com.teyvat.hub.data.content.CharacterRecord
import com.teyvat.hub.data.content.RegionRecord
import com.teyvat.hub.data.content.WeaponRecord
import com.teyvat.hub.data.local.character.CharacterEntity
import com.teyvat.hub.data.local.weapon.WeaponEntity
import com.teyvat.hub.domain.character.CharacterRole
import com.teyvat.hub.domain.character.Element
import com.teyvat.hub.domain.character.WeaponType

/**
 * Maps bundled content records into rows Room can store.
 *
 * A row whose keys this build does not recognise is dropped rather than
 * written with a fallback: a character filed under the wrong element, or a
 * weapon under the wrong family, is a defect the player can see, while a
 * character that is absent is not. Region names come from the bundle's own
 * region list, which is the only place that carries the display spelling;
 * the character rows store the lowercase key.
 */

/** Character bundle row to a roster row, or null when the row cannot be used. */
fun CharacterRecord.toEntity(regionNames: Map<String, String>): CharacterEntity? {
    val elementKey = Element.entries
        .firstOrNull { it.key == element }?.key
        ?: return null
    val weaponKey = WeaponType.entries
        .firstOrNull { it.key == weaponType }?.key
        ?: return null
    val roleKey = CharacterRole.entries
        .firstOrNull { it.key == role }?.key
        ?: return null
    return CharacterEntity(
        id = id,
        name = name,
        elementKey = elementKey,
        weaponTypeKey = weaponKey,
        rarityStars = if (rarity >= 5) 5 else 4,
        region = regionNames[region.lowercase()] ?: region,
        roleKey = roleKey,
        isFavorite = false,
    )
}

/** Weapon bundle row to a weapon row, or null when the row cannot be used. */
fun WeaponRecord.toEntity(): WeaponEntity? {
    val typeKey = WeaponType.entries
        .firstOrNull { it.key == type }?.key
        ?: return null
    return WeaponEntity(
        id = id,
        name = name,
        typeKey = typeKey,
        rarityStars = if (rarity >= 5) 5 else 4,
        baseAttack = baseAttack,
        subStat = subStat,
        subStatValue = subStatValue,
        passive = passive,
    )
}

/** Bundle region list into the lowercase-key to display-name map used above. */
fun List<RegionRecord>.toRegionNames(): Map<String, String> =
    associate { it.key.lowercase() to it.name }