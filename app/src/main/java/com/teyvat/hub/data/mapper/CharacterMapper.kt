package com.teyvat.hub.data.mapper

import com.teyvat.hub.data.local.character.CharacterEntity
import com.teyvat.hub.domain.model.Character
import com.teyvat.hub.domain.model.CharacterRole
import com.teyvat.hub.domain.model.Element
import com.teyvat.hub.domain.model.Rarity
import com.teyvat.hub.domain.model.WeaponType

/**
 * Converts a stored row into the domain model.
 *
 * Unknown keys fall back to safe defaults instead of throwing so a stale row
 * cannot crash the app after a content update; the API keys are whitelisted by
 * the enum itself.
 */
fun CharacterEntity.toDomain(): Character = Character(
    id = id,
    name = name,
    element = elementKey.toElement(),
    weaponType = weaponTypeKey.toWeaponType(),
    rarity = rarityStars.toRarity(),
    region = region,
    role = roleKey.toCharacterRole(),
    isFavorite = isFavorite,
)

fun String.toElement(): Element =
    Element.entries.firstOrNull { it.key == this } ?: Element.PHYSICAL

fun String.toWeaponType(): WeaponType =
    WeaponType.entries.firstOrNull { it.key == this } ?: WeaponType.SWORD

fun String.toCharacterRole(): CharacterRole =
    CharacterRole.entries.firstOrNull { it.key == this } ?: CharacterRole.UTILITY

fun Int.toRarity(): Rarity =
    if (this >= Rarity.FIVE.stars) Rarity.FIVE else Rarity.FOUR

fun Character.toEntity(): CharacterEntity = CharacterEntity(
    id = id,
    name = name,
    elementKey = element.key,
    weaponTypeKey = weaponType.key,
    rarityStars = rarity.stars,
    region = region,
    roleKey = role.key,
    isFavorite = isFavorite,
)
