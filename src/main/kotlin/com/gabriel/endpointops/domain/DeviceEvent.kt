package com.gabriel.endpointops.domain

import jakarta.persistence.*
import java.time.Instant
import java.util.*

@Entity
@Table(
    name = "device_events",
    indexes = [Index(name = "idx_device_id", columnList = "device_id")]
)
class DeviceEvent(
    @Id
    val id: UUID = UUID.randomUUID(),

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "device_id", nullable = false)
    val device: Device,

    @Column(nullable = false)
    val type: String,

    @Column(nullable = false)
    val createdAt: Instant = Instant.now(),

    @Column(columnDefinition = "text", nullable = false)
    val payload: String
)
