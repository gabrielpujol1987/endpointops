package com.gabriel.endpointops.domain

import jakarta.persistence.*
import java.time.Instant

@Entity
@Table(name = "devices")
class Device(
    @Id
    val id: String,

    @Column(nullable = false)
    var hostname: String,

    @Column(nullable = false)
    var lastSeenAt: Instant = Instant.EPOCH,

    @Column(nullable = false)
    var hitcount: Int = 0,
)
