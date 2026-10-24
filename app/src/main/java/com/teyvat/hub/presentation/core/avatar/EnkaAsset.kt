package com.teyvat.hub.presentation.core.avatar

/**
 * Resolves a character name to an optimized Genshin asset URL on the Enka CDN.
 *
 * Rationale in docs/adr/0003-optimized-genshin-digital-assets.md: only the
 * 12.7 KB side avatar is used in lists so per-row bitmaps cannot exhaust the
 * device heap; detail screens use the 34.3 KB variant.
 */
object EnkaAsset {

    private const val BASE = "https://enka.network/ui"

    /** Enka's file naming omits title prefixes and uses nicknames. */
    private val ALIASES = mapOf(
        "raiden shogun" to "raiden",
        "kaedehara kazuha" to "kazuha",
        "baal" to "raiden",
        "hu tao" to "hutao",
    )

    /**
     * Build the avatar URL for [name], or null when no asset exists for it.
     *
     * Unknown names resolve to null so callers can show a colour fallback
     * instead of a broken image.
     */
    fun avatarIcon(name: String): String? {
        val slug = normalize(name) ?: return null
        return "$BASE/UI_AvatarIcon_$slug.png"
    }

    /** Compact half-body avatar for list rows. */
    fun avatarSide(name: String): String? {
        val slug = normalize(name) ?: return null
        return "$BASE/UI_AvatarIcon_Side_$slug.png"
    }

    private fun normalize(rawName: String): String? {
        val trimmed = rawName.trim()
        if (trimmed.isBlank()) return null
        val keyLower = trimmed.lowercase()
        val alias = ALIASES[keyLower]
        if (alias != null) return encodeSlug(alias)
        // "First Last" assets on Enka only use the first token, preserving original case.
        val firstToken = trimmed.split(' ').firstOrNull { it.isNotBlank() } ?: return null
        return encodeSlug(firstToken)
    }

    private fun encodeSlug(slug: String): String =
        slug.replace(" ", "").replace("'", "")
            .replace("-", "").replace(".", "")
}
