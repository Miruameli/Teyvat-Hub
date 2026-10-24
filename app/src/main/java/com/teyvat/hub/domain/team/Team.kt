package com.teyvat.hub.domain.team

import com.teyvat.hub.domain.team.SynergyRule

/** Maximum party size allowed in Genshin Impact. */
const val MAX_TEAM_SIZE: Int = 4

/**
 * A saved team of up to [MAX_TEAM_SIZE] characters.
 *
 * [characterIds] are character ids in slot order. Games only allow four members,
 * so the domain rules reject anything larger before it reaches storage.
 *
 * [synergyRules] holds the names of the rules the analyzer reported, not their
 * wording. Storing the rule names keeps the note readable in whichever language
 * the player picks later, and keeps a translation from freezing the saved text
 * in the language that happened to be active when the team was stored.
 */
data class Team(
    val id: String,
    val name: String,
    val characterIds: List<String>,
    val synergyRules: List<String> = emptyList(),
    val isTemplate: Boolean = false,
)

/**
 * The rules the analyzer reports for this team, with any unknown names dropped
 * so a rule removed in a later version cannot break rendering.
 */
fun Team.synergyInsights(): List<SynergyInsight> =
    synergyRules.mapNotNull { name ->
        SynergyRule.entries.firstOrNull { it.name == name }?.let { SynergyInsight(it) }
    }
