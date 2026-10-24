package com.teyvat.hub.domain.team

import com.teyvat.hub.domain.character.Character
import com.teyvat.hub.domain.character.CharacterRole
import com.teyvat.hub.domain.character.Element

/**
 * Which rule a team note comes from.
 *
 * The domain decides the rule and never the wording: presentation resolves
 * each rule to text in the language the player chose, and tests can assert
 * on the rule instead of on a sentence that would have to be rewritten for
 * every translation.
 */
enum class SynergyRule {
    NO_REACTION,
    VAPORIZE,
    MELT,
    BLOOM,
    HAS_HEALER,
    HAS_SHIELD,
    NO_SUSTAIN,
    TOO_MANY_DPS,
    BALANCED_ROLES,
}

/** A checkable observation about one team, before it is worded for the player. */
data class SynergyInsight(val rule: SynergyRule)

/**
 * Derives practical notes about a team from its element spread and roles.
 *
 * Rules are intentionally explicit and small: each one explains a real, checkable
 * effect (a reaction, a missing healer, a duplicated role) so the note is
 * actionable rather than generic encouragement.
 */
class TeamSynergyAnalyzer {

    fun analyze(members: List<Character>): List<SynergyInsight> {
        if (members.isEmpty()) return emptyList()
        val insights = mutableListOf<SynergyInsight>()
        insights += describeReaction(members)
        insights += describeSupport(members)
        insights += describeRoleSpread(members)
        return insights
    }

    private fun describeReaction(members: List<Character>): SynergyInsight {
        val elements = members.map { it.element }.toSet()
        val rule = when {
            PYRO in elements && HYDRO in elements -> SynergyRule.VAPORIZE
            PYRO in elements && CRYO in elements -> SynergyRule.MELT
            HYDRO in elements && DENDRO in elements -> SynergyRule.BLOOM
            else -> SynergyRule.NO_REACTION
        }
        return SynergyInsight(rule)
    }

    private fun describeSupport(members: List<Character>): SynergyInsight {
        val roles = members.map { it.role }.toSet()
        val rule = when {
            HEALER in roles -> SynergyRule.HAS_HEALER
            SHIELD in roles -> SynergyRule.HAS_SHIELD
            else -> SynergyRule.NO_SUSTAIN
        }
        return SynergyInsight(rule)
    }

    private fun describeRoleSpread(members: List<Character>): SynergyInsight {
        val dpsCount = members.count {
            it.role == CharacterRole.MAIN_DPS || it.role == CharacterRole.SUB_DPS
        }
        return SynergyInsight(
            if (dpsCount >= 3) SynergyRule.TOO_MANY_DPS else SynergyRule.BALANCED_ROLES,
        )
    }

    private companion object {
        val PYRO = Element.PYRO
        val HYDRO = Element.HYDRO
        val CRYO = Element.CRYO
        val DENDRO = Element.DENDRO
        val HEALER = CharacterRole.HEALER
        val SHIELD = CharacterRole.SHIELD
    }
}
