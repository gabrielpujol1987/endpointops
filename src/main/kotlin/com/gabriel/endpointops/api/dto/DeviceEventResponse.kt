package com.gabriel.endpointops.api.dto

import java.time.Instant
import java.util.UUID

data class DeviceEventResponse(
    val id: UUID,
    val deviceId: String,
    val type: String,
    val createdAt: Instant,
    val payload: String
)
