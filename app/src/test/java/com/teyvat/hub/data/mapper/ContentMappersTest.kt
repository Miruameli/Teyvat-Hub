package com.teyvat.hub.data.mapper

import com.teyvat.hub.data.local.checklist.ChecklistItemEntity
import com.teyvat.hub.data.local.event.GameEventEntity
import com.teyvat.hub.data.local.team.TeamEntity
import com.teyvat.hub.domain.checklist.ResetCadence
import com.teyvat.hub.domain.event.EventStatus
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Locks the storage round trip so a refactor cannot silently drop data.
 *
 * Fallback behaviour matters here: unknown enum keys must not crash the app
 * after a content update, so they are asserted explicitly.
 */
class ContentMappersTest {

    @Test
    fun `checklist entity maps cadence and completion`() {
        val entity = ChecklistItemEntity(
            id = "daily-resin",
            title = "Gunakan resin",
            cadenceKey = "daily",
            lastCompletedAt = 1234L,
            isCustom = true,
        )

        val domain = entity.toDomain()

        assertEquals(ResetCadence.DAILY, domain.cadence)
        assertEquals(1234L, domain.lastCompletedAt)
        assertTrue(domain.isCustom)
    }

    @Test
    fun `checklist round trip preserves values`() {
        val entity = ChecklistItemEntity(
            id = "weekly-boss",
            title = "Boss mingguan",
            cadenceKey = "weekly",
            lastCompletedAt = null,
            isCustom = false,
        )

        val restored = entity.toDomain().toEntity()

        assertEquals(entity, restored)
    }

    @Test
    fun `unknown cadence key falls back to none`() {
        val entity = ChecklistItemEntity("x", "x", "mystery", null, false)

        assertEquals(ResetCadence.NONE, entity.toDomain().cadence)
    }

    @Test
    fun `team member ids survive delimiter round trip`() {
        val team = com.teyvat.hub.domain.team.Team(
            id = "t1",
            name = "Vaporize",
            characterIds = listOf("a", "b", "c", "d"),
            synergyRules = listOf("VAPORIZE", "HAS_HEALER"),
            isTemplate = true,
        )

        val restored = team.toEntity().toDomain()

        assertEquals(team.characterIds, restored.characterIds)
        assertEquals(team.name, restored.name)
        assertTrue(restored.isTemplate)
    }

    @Test
    fun `team with no members maps to empty list not blank string`() {
        val entity = TeamEntity("t1", "Empty", "", "", false)

        val domain = entity.toDomain()

        assertTrue(domain.characterIds.isEmpty())
    }

    @Test
    fun `event resolves active status at present time`() {
        val event = gameEvent(
            startsAt = 1_000L,
            endsAt = 5_000L,
        )

        assertEquals(EventStatus.ACTIVE, event.statusAt(3_000L))
    }

    @Test
    fun `event resolves ended status after window closes`() {
        val event = gameEvent(
            startsAt = 1_000L,
            endsAt = 5_000L,
        )

        assertEquals(EventStatus.ENDED, event.statusAt(6_000L))
    }

    @Test
    fun `event with no end date stays active`() {
        val event = GameEventEntity(
            id = "x",
            title = "Ongoing",
            kindKey = "event",
            startsAt = 1_000L,
            endsAt = null,
            description = "",
        )

        val domain = event.toDomain()

        assertNull(domain.endsAt)
        assertEquals(EventStatus.ACTIVE, domain.statusAt(9_000L))
    }

    /** Small helper keeping the status assertions readable. */
    private fun gameEvent(
        startsAt: Long,
        endsAt: Long?,
    ) = GameEventEntity(
        id = "x",
        title = "Event",
        kindKey = "event",
        startsAt = startsAt,
        endsAt = endsAt,
        description = "",
    ).toDomain()
}
