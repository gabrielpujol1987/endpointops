package com.gabriel.endpointops.service

import com.gabriel.endpointops.api.dto.PostEventRequest
import org.springframework.stereotype.Component
import java.util.concurrent.ArrayBlockingQueue

@Component
class EventQueue {
    private val queue = ArrayBlockingQueue<PostEventRequest>(10_000)

    fun offer(event: PostEventRequest): Boolean = queue.offer(event)

    fun take(): PostEventRequest = queue.take()

    fun size(): Int = queue.size
}
