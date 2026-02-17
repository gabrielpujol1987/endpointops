package com.gabriel.endpointops.service

import java.nio.charset.StandardCharsets
import java.time.Instant
import java.util.*

data class EventCursor(val createdAt: Instant, val id: UUID)

object CursorCodec {
    fun encode(c: EventCursor): String {
        val raw = "${c.createdAt.toEpochMilli()}:${c.id}"
        return Base64.getUrlEncoder().withoutPadding()
            .encodeToString(raw.toByteArray(StandardCharsets.UTF_8))
    }

    fun decode(cursor: String): EventCursor {
        try {
            val rawBytes = Base64.getUrlDecoder().decode(cursor)
            val raw = String(rawBytes, StandardCharsets.UTF_8)

            val parts = raw.split(":")
            if (parts.size != 2) throw InvalidCursorException("Invalid cursor format")

            val ts = parts[0].toLongOrNull() ?: throw InvalidCursorException("Invalid cursor timestamp")
            val id = runCatching { UUID.fromString(parts[1]) }
                .getOrElse { throw InvalidCursorException("Invalid cursor UUID") }

            return EventCursor(Instant.ofEpochMilli(ts), id)
        } catch (ex: IllegalArgumentException) {
            // Base64 decode failure
            throw InvalidCursorException("Cursor must be a valid base64url token")
        }
    }
}
