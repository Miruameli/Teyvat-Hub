package com.teyvat.hub.presentation.core.theme

import androidx.annotation.StringRes
import com.teyvat.hub.R
import com.teyvat.hub.domain.character.CharacterRole
import com.teyvat.hub.domain.character.Element
import com.teyvat.hub.domain.character.WeaponType
import com.teyvat.hub.domain.team.SynergyRule

/**
 * Player facing labels for the domain enums.
 *
 * The domain keeps stable machine keys such as "pyro" and "main-dps" because
 * they are stored in the database and must not change. Showing those keys to
 * the player would expose an internal identifier, so the presentation layer
 * maps them to string resources here.
 *
 * Resources are used instead of hardcoded text so the labels follow the
 * language chosen in settings, and so the domain stays free of wording.
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

/**
 * Wording for each team note rule.
 *
 * The analyzer reports the rule and never the sentence, so a note reads in
 * the chosen language instead of the Indonesian the rules were written in.
 */
private val SYNERGY_RULES: Map<SynergyRule, Pair<Int, Int>> = mapOf(
    SynergyRule.VAPORIZE to R.string.synergy_vaporize_headline to R.string.synergy_vaporize_detail,
    SynergyRule.MELT to R.string.synergy_melt_headline to R.string.synergy_melt_detail,
    SynergyRule.BLOOM to R.string.synergy_bloom_headline to R.string.synergy_bloom_detail,
    SynergyRule.NO_REACTION to R.string.synergy_no_reaction_headline to R.string.synergy_no_reaction_detail,
    SynergyRule.HAS_HEALER to R.string.synergy_has_healer_headline to R.string.synergy_has_healer_detail,
    SynergyRule.HAS_SHIELD to R.string.synergy_has_shield_headline to R.string.synergy_has_shield_detail,
    SynergyRule.NO_SUSTAIN to R.string.synergy_no_sustain_headline to R.string.synergy_no_sustain_detail,
    SynergyRule.TOO_MANY_DPS to R.string.synergy_too_many_dps_headline to R.string.synergy_too_many_dps_detail,
    SynergyRule.BALANCED_ROLES to R.string.synergy_balanced_roles_headline to R.string.synergy_balanced_roles_detail,
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

/** Headline wording for a team note. */
@StringRes
fun SynergyRule.headlineRes(): Int = SYNERGY_RULES[this]?.first ?: R.string.synergy_no_reaction_headline

/** Explanation wording for a team note. */
@StringRes
fun SynergyRule.detailRes(): Int = SYNERGY_RULES[this]?.second ?: R.string.synergy_no_reaction_detail
