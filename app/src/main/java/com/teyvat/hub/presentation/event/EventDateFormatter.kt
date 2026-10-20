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

/**
 * Server-timezone formatter using the Indonesian region language.
 *
 * [SimpleDateFormat] is not thread safe, so one instance is kept per thread
 * instead of a single shared field; a shared instance would interleave
 * partially formatted dates when two screens format concurrently.
 */
object DefaultEventDateFormatter : EventDateFormatter {

    private const val SERVER_ZONE = "Asia/Shanghai"
    private const val DAY_MONTH = "d MMM"

    private val formatters = ThreadLocal.withInitial {
        SimpleDateFormat(DAY_MONTH, Locale.forLanguageTag("id-ID")).apply {
            timeZone = TimeZone.getTimeZone(SERVER_ZONE)
        }
    }

    override fun range(startsAt: Long, endsAt: Long?): String {
        val formatter = formatters.get()
        val start = formatter.format(Date(startsAt))
        if (endsAt == null) return "$start -"
        return "$start - ${formatter.format(Date(endsAt))}"
    }
}
