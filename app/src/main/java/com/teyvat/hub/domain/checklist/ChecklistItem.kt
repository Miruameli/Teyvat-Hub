package com.teyvat.hub.domain.checklist

/** How often a checklist item resets to its unchecked state. */
enum class ResetCadence(val key: String) {
    DAILY("daily"),
    WEEKLY("weekly"),
    /** Never resets automatically; the player clears it by hand. */
    NONE("none"),
}

/**
 * A single farming task.
 *
 * [lastCompletedAt] is the epoch millis of the last completion, or null when
 * never completed. Reset rules live in the domain layer, not in the UI, so
 * that the same behaviour is testable without a device.
 */
data class ChecklistItem(
    val id: String,
    val title: String,
    val cadence: ResetCadence,
    val lastCompletedAt: Long? = null,
    val isCustom: Boolean = false,
) {
    /**
     * Whether the item should show as pending for the given reference time.
     *
     * An item completed inside its current cadence window is done; one
     * completed before the window start is pending again.
     */
    fun isPendingAt(now: Long, windowStart: Long): Boolean {
        val completed = lastCompletedAt ?: return true
        return completed < windowStart
    }
}
