package com.gabriel.endpointops.repo

import com.gabriel.endpointops.domain.DeviceEvent
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.time.Instant
import java.util.*

interface DeviceEventRepository : JpaRepository<DeviceEvent, UUID> {

    // First page (no cursor): simple, no OFFSET (we'll always use page=0 in cursor mode)
    fun findByDevice_IdOrderByCreatedAtDescIdDesc(deviceId: String, pageable: Pageable): List<DeviceEvent>

    fun findByDevice_Id(deviceId: String, pageable: Pageable): Page<DeviceEvent>

    fun findSliceByDevice_Id(deviceId: String, pageable: Pageable): Slice<DeviceEvent>

    // Next pages (cursor required): no null checks in SQL
    @Query(
        """
        select e from DeviceEvent e
        where e.device.id = :deviceId
          and (
            e.createdAt < :cursorCreatedAt
            or (e.createdAt = :cursorCreatedAt and e.id < :cursorId)
          )
        order by e.createdAt desc, e.id desc
        """
    )
    fun findByDeviceIdBeforeCursor(
        @Param("deviceId") deviceId: String,
        @Param("cursorCreatedAt") cursorCreatedAt: Instant,
        @Param("cursorId") cursorId: UUID,
        pageable: Pageable
    ): List<DeviceEvent>
}
