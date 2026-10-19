package com.teyvat.hub.data.seeding

import com.teyvat.hub.data.local.character.CharacterEntity

/**
 * Starter roster shipped inside the APK.
 *
 * The app is offline-first, so the database is seeded on first launch rather
 * than fetched from a server. Values mirror public game data; keeping this
 * list in code makes a content update a reviewable diff.
 */
object CharacterSeedContent {

    fun all(): List<CharacterEntity> = listOf(
        CharacterEntity(
            id = "albedo",
            name = "Albedo",
            elementKey = "geo",
            weaponTypeKey = "sword",
            rarityStars = 5,
            region = "Mondstadt",
            roleKey = "support",
            isFavorite = false,
        ),
        CharacterEntity(
            id = "kazuha",
            name = "Kaedehara Kazuha",
            elementKey = "anemo",
            weaponTypeKey = "sword",
            rarityStars = 5,
            region = "Inazuma",
            roleKey = "utility",
            isFavorite = false,
        ),
        CharacterEntity(
            id = "raiden",
            name = "Raiden Shogun",
            elementKey = "electro",
            weaponTypeKey = "polearm",
            rarityStars = 5,
            region = "Inazuma",
            roleKey = "support",
            isFavorite = false,
        ),
        CharacterEntity(
            id = "zhongli",
            name = "Zhongli",
            elementKey = "geo",
            weaponTypeKey = "polearm",
            rarityStars = 5,
            region = "Liyue",
            roleKey = "shield",
            isFavorite = false,
        ),
        CharacterEntity(
            id = "nahida",
            name = "Nahida",
            elementKey = "dendro",
            weaponTypeKey = "catalyst",
            rarityStars = 5,
            region = "Sumeru",
            roleKey = "sub-dps",
            isFavorite = false,
        ),
        CharacterEntity(
            id = "xiangling",
            name = "Xiangling",
            elementKey = "pyro",
            weaponTypeKey = "polearm",
            rarityStars = 4,
            region = "Liyue",
            roleKey = "sub-dps",
            isFavorite = false,
        ),
        CharacterEntity(
            id = "xingqiu",
            name = "Xingqiu",
            elementKey = "hydro",
            weaponTypeKey = "sword",
            rarityStars = 4,
            region = "Liyue",
            roleKey = "sub-dps",
            isFavorite = false,
        ),
        CharacterEntity(
            id = "bennett",
            name = "Bennett",
            elementKey = "pyro",
            weaponTypeKey = "sword",
            rarityStars = 4,
            region = "Mondstadt",
            roleKey = "healer",
            isFavorite = false,
        ),
    )
}
