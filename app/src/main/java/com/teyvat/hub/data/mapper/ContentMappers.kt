package com.teyvat.hub.data.mapper

import com.teyvat.hub.data.local.checklist.ChecklistItemEntity
import com.teyvat.hub.data.local.event.GameEventEntity
import com.teyvat.hub.data.local.team.TeamEntity
import com.teyvat.hub.domain.checklist.ChecklistItem
import com.teyvat.hub.domain.checklist.ResetCadence
import com.teyvat.hub.domain.event.EventKind
import com.teyvat.hub.domain.event.EventStatus
import com.teyvat.hub.domain.event.GameEvent
import com.teyvat.hub.domain.team.Team

/**
 * Maps stored rows to domain models and back.
 *
 * Enum keys are resolved defensively so content updates never crash a screen
 * because a row carries a key this build does not know yet.
 */

fun ChecklistItemEntity.toDomain(): ChecklistItem = ChecklistItem(
    id = id,
    title = title,
    cadence = cadenceKey.toResetCadence(),
    lastCompletedAt = lastCompletedAt,
    isCustom = isCustom,
)

fun ChecklistItem.toEntity(): ChecklistItemEntity = ChecklistItemEntity(
    id = id,
    title = title,
    cadenceKey = cadence.key,
    lastCompletedAt = lastCompletedAt,
    isCustom = isCustom,
)

fun String.toResetCadence(): ResetCadence =
    ResetCadence.entries.firstOrNull { it.key == this } ?: ResetCadence.NONE

fun TeamEntity.toDomain(): Team = Team(
    id = id,
    name = name,
    characterIds = memberIds.split(MEMBER_SEPARATOR).filter { it.isNotBlank() },
    synergyNotes = synergyNotes,
    isTemplate = isTemplate,
)

fun Team.toEntity(): TeamEntity = TeamEntity(
    id = id,
    name = name,
    memberIds = characterIds.joinToString(separator = MEMBER_SEPARATOR),
    synergyNotes = synergyNotes,
    isTemplate = isTemplate,
)

fun GameEventEntity.toDomain(): GameEvent = GameEvent(
    id = id,
    title = title,
    kind = kindKey.toEventKind(),
    startsAt = startsAt,
    endsAt = endsAt,
    description = description,
)

fun String.toEventKind(): EventKind =
    EventKind.entries.firstOrNull { it.key == this } ?: EventKind.EVENT

/** Delimiter used to persist a team's member ids in one column. */
private const val MEMBER_SEPARATOR = ","
