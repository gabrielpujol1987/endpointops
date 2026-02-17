package com.gabriel.endpointops.service

import jakarta.annotation.PostConstruct
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.util.concurrent.Executors

@Component
class EventIngestWorker(
    private val queue: EventQueue,
    private val deviceService: DeviceService
) {
    private val log = LoggerFactory.getLogger(javaClass)
    private val executor = Executors.newSingleThreadExecutor()

    @PostConstruct
    fun start() {
        executor.submit {
            while (!Thread.currentThread().isInterrupted) {
                try {
                    val event = queue.take()
                    deviceService.ingestEvent(event) // existing transactional method
                } catch (ex: Exception) {
                    log.error("Background ingest failed", ex)
                }
            }
        }
    }
}
