package com.teyvat.hub.data.content.seeding

import com.teyvat.hub.data.local.team.TeamEntity

/**
 * Starter team templates shipped inside the APK.
 *
 * Without these rows the teams table stays empty, the template section never
 * renders, and a player who saves a team has nothing to compare against.
 * Members reuse ids from [CharacterSeedContent] so every member resolves to a
 * real character row.
 */
object TeamSeedContent {

    fun all(): List<TeamEntity> = listOf(
        TeamEntity(
            id = "template-vapor",
            name = "Tim Uap Panas",
            memberIds = "xiangling,xingqiu,bennett,albedo",
            synergyNotes = "Vaporize dan support pyro",
            isTemplate = true,
        ),
        TeamEntity(
            id = "template-national",
            name = "Tim Nasional",
            memberIds = "xiangling,xingqiu,bennett,raiden",
            synergyNotes = "Akselerasi rotasi skill",
            isTemplate = true,
        ),
    )
}
