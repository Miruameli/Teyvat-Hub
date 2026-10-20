package com.teyvat.hub.domain.equipment

/**
 * An artifact set with its piece bonuses.
 *
 * [twoPiece] and [fourPiece] describe the bonuses at those piece counts; a
 * [fourPiece] of empty string means the set has no four-piece bonus.
 */
data class ArtifactSet(
    val id: String,
    val name: String,
    val twoPiece: String,
    val fourPiece: String = "",
    val domain: String,
    val recommendedFor: List<String> = emptyList(),
)
