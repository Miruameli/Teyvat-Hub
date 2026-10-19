package com.teyvat.hub.data.local.asset

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.teyvat.hub.domain.model.Character
import com.teyvat.hub.domain.model.Element
import com.teyvat.hub.domain.model.Region
import com.teyvat.hub.domain.model.Weapon
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.lang.reflect.Type

/**
 * Loads all game data from local JSON assets - no network required.
 *
 * All character, weapon, element, and region data is bundled with the app
 * so it works completely offline. This replaces the old EnkaAsset network calls.
 */
class AssetManager(private val context: Context) {

    private val gson = Gson()

    /**
     * Loads all characters from local JSON asset.
     */
    fun getCharacters(): Flow<List<Character>> = flow {
        val json = readAsset("characters/characters.json")
        val type = object : TypeToken<List<Character>>() {}.type
        val characters = gson.fromJson<List<Character>>(json, type)
        emit(characters)
    }

    /**
     * Loads all weapons from local JSON asset.
     */
    fun getWeapons(): Flow<List<Weapon>> = flow {
        val json = readAsset("weapons/weapons.json")
        val type = object : TypeToken<List<Weapon>>() {}.type
        val weapons = gson.fromJson<List<Weapon>>(json, type)
        emit(weapons)
    }

    /**
     * Loads all elements from local JSON asset.
     */
    fun getElements(): Flow<List<Element>> = flow {
        val json = readAsset("elements/elements.json")
        val type = object : TypeToken<List<Element>>() {}.type
        val elements = gson.fromJson<List<Element>>(json, type)
        emit(elements)
    }

    /**
     * Loads all regions from local JSON asset.
     */
    fun getRegions(): Flow<List<Region>> = flow {
        val json = readAsset("regions/regions.json")
        val type = object : TypeToken<List<Region>>() {}.type
        val regions = gson.fromJson<List<Region>>(json, type)
        emit(regions)
    }

    /**
     * Reads raw string from assets folder.
     */
    private fun readAsset(path: String): String {
        val inputStream = context.assets.open(path)
        return inputStream.bufferedReader().use { it.readText() }
    }
}