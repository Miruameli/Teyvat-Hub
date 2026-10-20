package com.teyvat.hub.domain.team

/** Maximum party size allowed in Genshin Impact. */
const val MAX_TEAM_SIZE: Int = 4

/**
 * A saved team of up to [MAX_TEAM_SIZE] characters.
 *
 * [characterIds] are character ids in slot order. Games only allow four members,
 * so the domain rules reject anything larger before it reaches storage.
 */
data class Team(
    val id: String,
    val name: String,
    val characterIds: List<String>,
    val synergyNotes: String = "",
    val isTemplate: Boolean = false,
)
