package com.teyvat.hub.domain.character

/** Elemental affinity of a character or reaction. */
enum class Element(val key: String) {
    ANEMO("anemo"),
    GEO("geo"),
    ELECTRO("electro"),
    DENDRO("dendro"),
    HYDRO("hydro"),
    PYRO("pyro"),
    CRYO("cryo"),
    PHYSICAL("physical"),
}

/** Weapon family a character can use, matching the in-game weapon slots. */
enum class WeaponType(val key: String) {
    SWORD("sword"),
    CLAYMORE("claymore"),
    POLEARM("polearm"),
    BOW("bow"),
    CATALYST("catalyst"),
}

/** Rarity tier shown as stars on cards. */
enum class Rarity(val stars: Int) {
    FOUR(4),
    FIVE(5),
}

/**
 * A playable character with the fields the database needs.
 *
 * Lives in [domain] so presentation code can render cards without touching
 * Room entities, keeping business rules testable on the JVM.
 */
data class Character(
    val id: String,
    val name: String,
    val element: Element,
    val weaponType: WeaponType,
    val rarity: Rarity,
    val region: String,
    val role: CharacterRole,
    val isFavorite: Boolean = false,
)

/** Role a character typically fills inside a team. */
enum class CharacterRole(val key: String) {
    MAIN_DPS("main-dps"),
    SUB_DPS("sub-dps"),
    SUPPORT("support"),
    SHIELD("shield"),
    HEALER("healer"),
    UTILITY("utility"),
}
