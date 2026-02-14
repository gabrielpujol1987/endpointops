package com.gabriel.endpointops.api.dto

import jakarta.validation.constraints.NotBlank

data class PostEventRequest(
    @field:NotBlank val deviceId: String,
    @field:NotBlank val hostname: String,
    @field:NotBlank val type: String,
    @field:NotBlank val payload: String
)
