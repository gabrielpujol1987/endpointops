package com.gabriel.endpointops.repo

import com.gabriel.endpointops.domain.DeviceEvent
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface DeviceEventRepository : JpaRepository<DeviceEvent, UUID> {
    fun findByDevice_Id(deviceId: String, pageable: Pageable): Slice<DeviceEvent>

}
