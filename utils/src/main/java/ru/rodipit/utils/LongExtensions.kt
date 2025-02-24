package ru.rodipit.utils

import java.util.Locale
import kotlin.time.Duration.Companion.seconds

fun Long.toFormattedTimeDuration(): String {
    return this.seconds.toComponents { hours, minutes, seconds, _ ->
        String.format(
            Locale.getDefault(),
            "%02d:%02d:%02d",
            hours,
            minutes,
            seconds,
        )
    }
}