package com.morarfilip.githubexplorer.core.common

import java.util.Locale
import java.text.SimpleDateFormat
import java.util.TimeZone

object DateFormatter {
    fun formatIsoDate(isoString: String): String {
        return try {
            // 1. Define the input format (GitHub's ISO 8601 format)
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US).apply {
                timeZone = TimeZone.getTimeZone("UTC")
            }

            // 2. Define the output format (User-friendly)
            val outputFormat = SimpleDateFormat("MMM dd, yyyy - hh:mm a", Locale.getDefault()).apply {
                timeZone = TimeZone.getTimeZone("UTC")
            }

            // 3. Parse and re-format
            val date = inputFormat.parse(isoString)
            if (date != null) {
                outputFormat.format(date)
            } else {
                isoString
            }
        } catch (_: Exception) {
            isoString
        }
    }
}