package it.polito.wa2.ecommerce.orderservice.exception

import it.polito.wa2.ecommerce.common.exceptions.NotFoundException

class OrderNotFoundException(orderId: Long) : NotFoundException("Cannot find order $orderId")
