package it.polito.wa2.ecommerce.common

import org.springframework.data.domain.PageRequest

fun getPageable(pageIdx: Int, pageSize: Int) =
    PageRequest.of(
        pageIdx - 1, // pageable index starts from 0, while API have indexes starting from 1
        pageSize
    )