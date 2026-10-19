package com.teyvat.hub.domain.service

import com.teyvat.hub.domain.model.Character
import com.teyvat.hub.domain.model.CharacterRole
import com.teyvat.hub.domain.model.Element

/** Insight about one team, surfaced on the team builder screen. */
data class SynergyInsight(
    val headline: String,
    val detail: String,
)

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
        return when {
            PYRO in elements && HYDRO in elements ->
                SynergyInsight(
                    headline = "Reaksi Vaporize aktif",
                    detail = "Pasangan Pyro + Hydro memberi bonus kerusakan besar.",
                )
            PYRO in elements && CRYO in elements ->
                SynergyInsight(
                    headline = "Reaksi Melt aktif",
                    detail = "Cryo diikuti Pyro meningkatkan kerusakan serangan.",
                )
            HYDRO in elements && DENDRO in elements ->
                SynergyInsight(
                    headline = "Reaksi Bloom aktif",
                    detail = "Hydro + Dendro memberi kerusakan area berkelanjutan.",
                )
            else ->
                SynergyInsight(
                    headline = "Belum ada reaksi utama",
                    detail = "Tambahkan elemen berbeda untuk membuka reaksi elemental.",
                )
        }
    }

    private fun describeSupport(members: List<Character>): SynergyInsight {
        val roles = members.map { it.role }.toSet()
        return when {
            CharacterRole.HEALER in roles ->
                SynergyInsight(
                    headline = "Penyembuh tersedia",
                    detail = "Tim punya sustain sehingga cocok untuk konten panjang.",
                )
            CharacterRole.SHIELD in roles ->
                SynergyInsight(
                    headline = "Perisai tersedia",
                    detail = "Perisai mengurangi tekanan HP walau tanpa penyembih.",
                )
            else ->
                SynergyInsight(
                    headline = "Tanpa sustain",
                    detail = "Pertimbangkan penyembih atau perisai untuk bertahan hidup.",
                )
        }
    }

    private fun describeRoleSpread(members: List<Character>): SynergyInsight {
        val dpsCount = members.count {
            it.role == CharacterRole.MAIN_DPS || it.role == CharacterRole.SUB_DPS
        }
        return if (dpsCount >= 3) {
            SynergyInsight(
                headline = "Terlalu banyak DPS",
                detail = "Kurangi DPS dan tambahkan dukungan agar kerusakan lebih stabil.",
            )
        } else {
            SynergyInsight(
                headline = "Peran seimbang",
                detail = "Kombinasi peran mendukung kerusakan yang konsisten.",
            )
        }
    }

    private companion object {
        val PYRO = Element.PYRO
        val HYDRO = Element.HYDRO
        val CRYO = Element.CRYO
        val DENDRO = Element.DENDRO
    }
}
