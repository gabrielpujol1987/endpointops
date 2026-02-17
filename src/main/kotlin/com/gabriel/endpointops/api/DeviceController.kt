package com.gabriel.endpointops.api

import com.gabriel.endpointops.api.dto.DeviceEventResponse
import com.gabriel.endpointops.api.dto.DeviceResponse
import com.gabriel.endpointops.api.dto.PagedResponse
import com.gabriel.endpointops.api.dto.PostEventRequest
import com.gabriel.endpointops.service.DeviceService
import com.gabriel.endpointops.service.EventQueue
import jakarta.validation.Valid
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api")
class DeviceController(
    private val service: DeviceService,
    private val queue: EventQueue,
) {
    @PostMapping("/events")
    fun postEvent(@RequestBody @Valid req: PostEventRequest): ResponseEntity<Void> {
        val accepted = queue.offer(req)
        return if (accepted) {
            ResponseEntity.accepted().build()
        } else {
            ResponseEntity.status(429).build()
        }
    }

    @GetMapping("/devices/{id}")
    fun getDevice(@PathVariable id: String): DeviceResponse =
        service.getDevice(id)

    @GetMapping("/devices/{id}/events")
    fun getDeviceEvents(
        @PathVariable id: String,
        pageable: Pageable,                                 // page/size/sort (if mode=PAGE o SLICE)
        @RequestParam(required = false) cursor: String?     // if mode=CURSOR
    ): PagedResponse<DeviceEventResponse> =
        service.getDeviceEvents(id, pageable, cursor)

    @GetMapping("/queue/size")
    fun getQueueSize(): Int =
        queue.size()

}
