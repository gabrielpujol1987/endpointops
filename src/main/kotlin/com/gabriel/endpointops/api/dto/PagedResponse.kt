package com.gabriel.endpointops.api.dto

data class PagedResponse<T>(
    val items: List<T>,
    val page: Int? = null,
    val size: Int,
    val hasNext: Boolean,
    val totalElements: Long? = null,
    val nextCursor: String? = null
)
