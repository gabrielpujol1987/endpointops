package com.gabriel.endpointops.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "endpointops.pagination")
class PaginationProperties {
    var mode: Mode = Mode.CURSOR
    var defaultSize: Int = 50
    var maxSize: Int = 100

    enum class Mode { PAGE, SLICE, CURSOR }
}
