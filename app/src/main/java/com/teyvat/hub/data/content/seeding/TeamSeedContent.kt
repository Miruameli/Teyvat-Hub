package com.teyvat.hub.data.content.seeding

import com.teyvat.hub.data.local.team.TeamEntity

/**
 * Starter team templates shipped inside the APK.
 *
 * Without these rows the teams table stays empty, the template section never
 * renders, and a player who saves a team has nothing to compare against.
 * Members reuse ids from the bundled roster, so every member resolves to a
 * real character row; a template naming a character the bundle does not carry
 * would render as a broken slot instead of a team.
 */
object TeamSeedContent {

    fun all(): List<TeamEntity> = listOf(
        TeamEntity(
            id = "template-traveler-vaporize",
            name = "Tim Uap Pemula",
            memberIds = "amber,barbara,kaeya,traveler_anemo",
            synergyRules = "VAPORIZE,HAS_HEALER,BALANCED_ROLES",
            isTemplate = true,
        ),
        TeamEntity(
            id = "template-overload",
            name = "Tim Overload Mondstadt",
            memberIds = "fischl,lisa,bennett,venti",
            synergyRules = "NO_REACTION,NO_SUSTAIN,BALANCED_ROLES",
            isTemplate = true,
        ),
    )
}
