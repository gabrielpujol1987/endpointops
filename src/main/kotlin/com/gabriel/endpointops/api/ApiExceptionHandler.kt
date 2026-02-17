package com.gabriel.endpointops.api

import com.gabriel.endpointops.service.InvalidCursorException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ApiExceptionHandler {

    @ExceptionHandler(InvalidCursorException::class)
    fun badCursor(ex: InvalidCursorException): ResponseEntity<Map<String, String>> =
        ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(mapOf("error" to (ex.message ?: "Invalid cursor")))

    @ExceptionHandler(NoSuchElementException::class)
    fun notFound(ex: NoSuchElementException): ResponseEntity<Map<String, String>> =
        ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(mapOf("error" to ex.message.orEmpty()))
}
