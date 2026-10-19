package com.teyvat.hub.domain.service

import com.teyvat.hub.domain.model.ChecklistItem
import com.teyvat.hub.domain.model.ResetCadence
import java.util.Calendar
import java.util.TimeZone

/**
 * Computes the start of the reset window for a given cadence.
 *
 * Genshin Impact resets daily content at 04:00 (UTC+8), which is 20:00 UTC on
 * the previous day. Container time is kept here rather than in the UI so the
 * rule can be unit tested without a device clock.
 */
class ChecklistResetCalculator(
    private val serverTimeZone: TimeZone = TimeZone.getTimeZone("Asia/Shanghai"),
    private val resetHour: Int = 4,
) {

    /**
     * Epoch millis of the current window start for [item] at [now].
     *
     * For daily items this is the most recent 04:00 server time. Weekly items
     * reset every Monday at the same hour.
     */
    fun windowStart(item: ChecklistItem, now: Long): Long {
        val calendar = Calendar.getInstance(serverTimeZone)
        calendar.timeInMillis = now

        return when (item.cadence) {
            ResetCadence.NONE -> MIN_TIME
            ResetCadence.DAILY -> startOfDay(calendar, now)
            ResetCadence.WEEKLY -> startOfWeek(calendar, now)
        }
    }

    private fun startOfDay(calendar: Calendar, now: Long): Long {
        calendar.set(Calendar.HOUR_OF_DAY, resetHour)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        if (calendar.timeInMillis > now) {
            calendar.add(Calendar.DAY_OF_YEAR, -1)
        }
        return calendar.timeInMillis
    }

    private fun startOfWeek(calendar: Calendar, now: Long): Long {
        // ISO weeks start on Monday, so Sunday must roll back one day.
        var dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
        if (dayOfWeek == Calendar.SUNDAY) {
            dayOfWeek = Calendar.MONDAY + 7
        }
        val daysFromMonday = dayOfWeek - Calendar.MONDAY
        calendar.add(Calendar.DAY_OF_YEAR, -daysFromMonday)
        val weekStart = startOfDay(calendar, now)
        return weekStart
    }

    private companion object {
        const val MIN_TIME = 0L
    }
}
