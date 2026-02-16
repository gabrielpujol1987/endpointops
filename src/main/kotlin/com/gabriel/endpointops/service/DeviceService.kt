package com.gabriel.endpointops.service

import com.gabriel.endpointops.api.dto.DeviceResponse
import com.gabriel.endpointops.api.dto.PostEventRequest
import com.gabriel.endpointops.domain.Device
import com.gabriel.endpointops.domain.DeviceEvent
import com.gabriel.endpointops.repo.DeviceEventRepository
import com.gabriel.endpointops.repo.DeviceRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant

@Service
class DeviceService(
    private val deviceRepo: DeviceRepository,
    private val eventRepo: DeviceEventRepository
) {
    @Transactional
    fun ingestEvent(req: PostEventRequest) {
        val now = Instant.now()

        val device = deviceRepo.findById(req.deviceId).orElseGet {
            Device(id = req.deviceId, hostname = req.hostname, lastSeenAt = now)
        }.also {
            it.hostname = req.hostname
            it.lastSeenAt = now
            it.hitcount += 1
        }

        deviceRepo.save(device)
        eventRepo.save(
            DeviceEvent(
                device = device,
                type = req.type,
                payload = req.payload
            )
        )

        print("hitcount: '${device.hitcount}'")
    }

    @Transactional(readOnly = true)
    fun getDevice(id: String): DeviceResponse {
        val device = deviceRepo.findById(id).orElseThrow {
            NoSuchElementException("Device not found: $id")
        }
        return DeviceResponse(device.id, device.hostname, device.lastSeenAt)
    }
}
