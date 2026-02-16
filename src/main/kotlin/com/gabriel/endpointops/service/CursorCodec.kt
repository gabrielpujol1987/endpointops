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
        val raw = String(Base64.getUrlDecoder().decode(cursor), StandardCharsets.UTF_8)
        val parts = raw.split(":")
        require(parts.size == 2) { "Invalid cursor format" }
        val ts = parts[0].toLong()
        val id = UUID.fromString(parts[1])
        return EventCursor(Instant.ofEpochMilli(ts), id)
    }
}
