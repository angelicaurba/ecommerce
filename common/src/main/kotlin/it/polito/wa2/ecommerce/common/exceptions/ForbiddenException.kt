package it.polito.wa2.ecommerce.common.exceptions

open class ForbiddenException(message: String = "Unauthorized action"): Exception(message)