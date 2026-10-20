package com.teyvat.hub.presentation.theme

import androidx.annotation.StringRes
import com.teyvat.hub.R
import com.teyvat.hub.domain.model.CharacterRole
import com.teyvat.hub.domain.model.Element
import com.teyvat.hub.domain.model.WeaponType

/**
 * Indonesian labels for the domain enums.
 *
 * The domain keeps stable machine keys such as "pyro" and "main-dps" because
 * they are stored in the database and must not change. Showing those keys to
 * the player would expose an internal identifier, so the presentation layer
 * maps them to string resources here.
 *
 * Resources are used instead of hardcoded text so the labels can be
 * translated when the settings screen offers a language choice.
 */

private val ELEMENT_LABELS: Map<Element, Int> = mapOf(
    Element.ANEMO to R.string.element_anemo,
    Element.GEO to R.string.element_geo,
    Element.ELECTRO to R.string.element_electro,
    Element.DENDRO to R.string.element_dendro,
    Element.HYDRO to R.string.element_hydro,
    Element.PYRO to R.string.element_pyro,
    Element.CRYO to R.string.element_cryo,
    Element.PHYSICAL to R.string.element_physical,
)

private val ROLE_LABELS: Map<CharacterRole, Int> = mapOf(
    CharacterRole.MAIN_DPS to R.string.role_main_dps,
    CharacterRole.SUB_DPS to R.string.role_sub_dps,
    CharacterRole.SUPPORT to R.string.role_support,
    CharacterRole.SHIELD to R.string.role_shield,
    CharacterRole.HEALER to R.string.role_healer,
    CharacterRole.UTILITY to R.string.role_utility,
)

private val WEAPON_LABELS: Map<WeaponType, Int> = mapOf(
    WeaponType.SWORD to R.string.weapon_sword,
    WeaponType.CLAYMORE to R.string.weapon_claymore,
    WeaponType.POLEARM to R.string.weapon_polearm,
    WeaponType.BOW to R.string.weapon_bow,
    WeaponType.CATALYST to R.string.weapon_catalyst,
)

/** Player facing element name, for example "Pyro". */
@StringRes
fun Element.labelRes(): Int = ELEMENT_LABELS[this] ?: R.string.element_unknown

/** Player facing role name, for example "DPS Utama". */
@StringRes
fun CharacterRole.labelRes(): Int = ROLE_LABELS[this] ?: R.string.role_unknown

/** Player facing weapon family name, for example "Pedang". */
@StringRes
fun WeaponType.labelRes(): Int = WEAPON_LABELS[this] ?: R.string.weapon_unknown
