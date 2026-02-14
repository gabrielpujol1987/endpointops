package com.gabriel.endpointops.api

import com.gabriel.endpointops.api.dto.DeviceResponse
import com.gabriel.endpointops.api.dto.PostEventRequest
import com.gabriel.endpointops.service.DeviceService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api")
class DeviceController(
    private val service: DeviceService
) {
    @PostMapping("/events")
    fun postEvent(@RequestBody @Valid req: PostEventRequest): ResponseEntity<Void> {
        service.ingestEvent(req)
        return ResponseEntity.accepted().build()
    }

    @GetMapping("/devices/{id}")
    fun getDevice(@PathVariable id: String): DeviceResponse =
        service.getDevice(id)
}
