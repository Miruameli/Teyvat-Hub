package com.teyvat.hub.data.content

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * Reads the bundled content bundles into plain records.
 *
 * The app is offline-first, so the roster and weapons have to exist in local
 * storage before any screen renders. Records rather than Room entities keep
 * this loader free of persistence concerns, and a records-based mapper is
 * where unknown enum keys are resolved deliberately instead of at read time.
 */
class AssetContentLoader(private val context: Context) {

    private val gson = Gson()

    fun characters(): List<CharacterRecord> = readList("characters/characters.json")

    fun weapons(): List<WeaponRecord> = readList("weapons/weapons.json")

    fun regions(): List<RegionRecord> = readList("regions/regions.json")

    /**
     * Parses a bundled JSON array of the given record type.
     *
     * Gson is told the element type explicitly because a bare `List` would
     * erase it to `LinkedTreeMap`, leaving every field null instead of a
     * readable record.
     */
    private fun <T : Any> readList(path: String): List<T> {
        val json = context.assets.open(path).bufferedReader()
            .use { reader -> reader.readText() }
        val type = TypeToken.getParameterized(List::class.java, recordType(path)).type
        @Suppress("UNCHECKED_CAST")
        return gson.fromJson(json, type) as? List<T> ?: emptyList()
    }

    private fun recordType(path: String): Class<*> = when (path) {
        "characters/characters.json" -> CharacterRecord::class.java
        "weapons/weapons.json" -> WeaponRecord::class.java
        "regions/regions.json" -> RegionRecord::class.java
        else -> throw IllegalArgumentException("Unsupported bundle: $path")
    }
}
