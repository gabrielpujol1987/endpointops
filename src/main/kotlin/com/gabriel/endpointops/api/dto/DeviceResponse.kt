package com.gabriel.endpointops.api.dto

import java.time.Instant

data class DeviceResponse(
    val id: String,
    val hostname: String,
    val lastSeenAt: Instant
)
