package com.gabriel.endpointops.repo

import com.gabriel.endpointops.domain.Device
import org.springframework.data.jpa.repository.JpaRepository

interface DeviceRepository : JpaRepository<Device, String>
