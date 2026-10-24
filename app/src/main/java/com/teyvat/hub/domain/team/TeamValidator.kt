package com.teyvat.hub.domain.team

import com.teyvat.hub.domain.character.Character

/**
 * Which rule stopped a team from being saved.
 *
 * The domain reports the rule and presentation supplies the wording, so the
 * reason is readable in the language the player chose and the rules stay
 * testable without locale data.
 */
enum class TeamInvalidRule {
    EMPTY,
    TOO_LARGE,
    DUPLICATE_MEMBER,
    NO_NAME,
}

/** Outcome of validating a team before it is saved. */
sealed interface TeamValidation {
    data object Valid : TeamValidation
    data class Invalid(val rule: TeamInvalidRule) : TeamValidation
}

/**
 * Enforces the rules a team must satisfy before storage.
 *
 * Genshin Impact allows exactly four members and forbids duplicating a
 * character, so both constraints are checked here rather than in the UI so
 * they cannot be bypassed by a new screen that forgets them.
 */
class TeamValidator {

    fun validate(team: Team): TeamValidation = when {
        team.characterIds.isEmpty() -> TeamValidation.Invalid(TeamInvalidRule.EMPTY)
        team.characterIds.size > MAX_TEAM_SIZE -> TeamValidation.Invalid(TeamInvalidRule.TOO_LARGE)
        team.characterIds.distinct().size != team.characterIds.size ->
            TeamValidation.Invalid(TeamInvalidRule.DUPLICATE_MEMBER)
        team.name.isBlank() -> TeamValidation.Invalid(TeamInvalidRule.NO_NAME)
        else -> TeamValidation.Valid
    }

    /** True when a character may still be added to the current selection. */
    fun canAdd(current: List<Character>): Boolean =
        current.size < MAX_TEAM_SIZE &&
            current.map { it.id }.distinct().size == current.size
}
