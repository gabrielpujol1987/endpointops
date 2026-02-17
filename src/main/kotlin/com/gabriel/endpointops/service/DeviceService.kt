package com.gabriel.endpointops.service

import com.gabriel.endpointops.api.dto.DeviceEventResponse
import com.gabriel.endpointops.api.dto.DeviceResponse
import com.gabriel.endpointops.api.dto.PagedResponse
import com.gabriel.endpointops.api.dto.PostEventRequest
import com.gabriel.endpointops.config.PaginationProperties
import com.gabriel.endpointops.domain.Device
import com.gabriel.endpointops.domain.DeviceEvent
import com.gabriel.endpointops.repo.DeviceEventRepository
import com.gabriel.endpointops.repo.DeviceRepository
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant

@Service
class DeviceService(
    private val deviceRepo: DeviceRepository,
    private val eventRepo: DeviceEventRepository,
    private val paginationProps: PaginationProperties,
) {
    @Transactional
    fun ingestEvent(req: PostEventRequest) {
        val now = Instant.now()

        val device = deviceRepo.findById(req.deviceId).orElseGet {
            Device(id = req.deviceId, hostname = req.hostname, lastSeenAt = now, hitcount = 1)
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
        return DeviceResponse(device.id, device.hostname, device.lastSeenAt, device.hitcount)
    }

    @Transactional(readOnly = true)
    fun getDeviceEvents(
        deviceId: String,
        pageable: Pageable?,
        cursor: String?
    ): PagedResponse<DeviceEventResponse> {

        val size = clampSize(pageable?.pageSize ?: paginationProps.defaultSize)

        return when (paginationProps.mode) {
            PaginationProperties.Mode.PAGE -> {
                val pageReq = PageRequest.of(
                    pageable?.pageNumber ?: 0,
                    size,
                    pageable?.sort?.takeIf { it.isSorted } ?: defaultSort
                )

                val page = eventRepo.findByDevice_Id(deviceId, pageReq)
                PagedResponse(
                    items = page.content.map(::toDto),
                    page = page.number,
                    size = page.size,
                    hasNext = page.hasNext(),
                    totalElements = page.totalElements
                )
            }

            PaginationProperties.Mode.SLICE -> {
                val sliceReq = PageRequest.of(
                    pageable?.pageNumber ?: 0,
                    size,
                    pageable?.sort?.takeIf { it.isSorted } ?: defaultSort
                )

                val slice = eventRepo.findSliceByDevice_Id(deviceId, sliceReq)
                PagedResponse(
                    items = slice.content.map(::toDto),
                    page = slice.number,
                    size = slice.size,
                    hasNext = slice.hasNext(),
                    totalElements = null
                )
            }

            PaginationProperties.Mode.CURSOR -> {
                val limitReq = PageRequest.of(0, size) // no sort here

                val rows = if (cursor.isNullOrBlank()) {
                    eventRepo.findByDevice_IdOrderByCreatedAtDescIdDesc(deviceId, limitReq)
                } else {
                    val decoded = CursorCodec.decode(cursor)
                    eventRepo.findByDeviceIdBeforeCursor(
                        deviceId = deviceId,
                        cursorCreatedAt = decoded.createdAt,
                        cursorId = decoded.id,
                        pageable = limitReq
                    )
                }

                val items = rows.map(::toDto)
                val next = rows.lastOrNull()?.let { CursorCodec.encode(EventCursor(it.createdAt, it.id)) }

                PagedResponse(
                    items = items,
                    page = null,
                    size = size,
                    hasNext = rows.size == size,
                    totalElements = null,
                    nextCursor = next
                )
            }
        }
    }

    private val defaultSort = Sort.by(Sort.Direction.DESC, "createdAt").and(
        Sort.by(Sort.Direction.DESC, "id")
    )

    private fun clampSize(size: Int): Int =
        size.coerceAtLeast(1).coerceAtMost(paginationProps.maxSize)

    private fun toDto(e: DeviceEvent) =
        DeviceEventResponse(
            id = e.id,
            deviceId = e.device.id,
            type = e.type,
            createdAt = e.createdAt,
            payload = e.payload
        )

}
