package com.teyvat.hub.presentation.event

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

/**
 * Turns stored epoch millis into a compact date range.
 *
 * An interface (rather than a direct date call) keeps the state builder pure:
 * tests substitute a fixed formatter so no assertion depends on the host clock
 * or on locale data installed on the CI machine.
 */
interface EventDateFormatter {
    /** Human range such as "16 Okt - 28 Okt". */
    fun range(startsAt: Long, endsAt: Long?): String
}

/** Server-timezone formatter; the region language is Indonesian. */
object DefaultEventDateFormatter : EventDateFormatter {

    private const val SERVER_ZONE = "Asia/Shanghai"
    private const val DAY_MONTH = "d MMM"

    private val dayMonth = SimpleDateFormat(DAY_MONTH, Locale("in", "ID")).apply {
        timeZone = TimeZone.getTimeZone(SERVER_ZONE)
    }

    override fun range(startsAt: Long, endsAt: Long?): String {
        val start = dayMonth.format(Date(startsAt))
        if (endsAt == null) return "$start -"
        return "$start - ${dayMonth.format(Date(endsAt))}"
    }
}
