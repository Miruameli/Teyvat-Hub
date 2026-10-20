package com.teyvat.hub.domain.team

import com.teyvat.hub.domain.character.Character

/** Outcome of validating a team before it is saved. */
sealed interface TeamValidation {
    data object Valid : TeamValidation
    data class Invalid(val reason: String) : TeamValidation
}

/**
 * Enforces the rules a team must satisfy before storage.
 *
 * Genshin Impact allows exactly four members and forbids duplicating a
 * character, so both constraints are checked here rather than in the UI so
 * they cannot be bypassed by a new screen that forgets them.
 */
class TeamValidator {

    fun validate(team: Team): TeamValidation {
        if (team.characterIds.isEmpty()) {
            return TeamValidation.Invalid("Tim minimal berisi satu karakter")
        }
        if (team.characterIds.size > MAX_TEAM_SIZE) {
            return TeamValidation.Invalid("Tim maksimal $MAX_TEAM_SIZE karakter")
        }
        if (team.characterIds.distinct().size != team.characterIds.size) {
            return TeamValidation.Invalid("Karakter tidak boleh ganda dalam tim")
        }
        if (team.name.isBlank()) {
            return TeamValidation.Invalid("Nama tim tidak boleh kosong")
        }
        return TeamValidation.Valid
    }

    /** True when a character may still be added to the current selection. */
    fun canAdd(current: List<Character>): Boolean =
        current.size < MAX_TEAM_SIZE &&
            current.map { it.id }.distinct().size == current.size
}
