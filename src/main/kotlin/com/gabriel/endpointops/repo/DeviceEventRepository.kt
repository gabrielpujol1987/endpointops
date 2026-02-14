package com.gabriel.endpointops.repo

import com.gabriel.endpointops.domain.DeviceEvent
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface DeviceEventRepository : JpaRepository<DeviceEvent, UUID>
