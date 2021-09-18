package it.polito.wa2.ecommerce.warehouseservice.exception

import it.polito.wa2.ecommerce.common.exceptions.NotFoundException

class ItemsNotAvailable (productId: String, amount: Int)
: NotFoundException("Items not available, cannot find a stock with productId $productId or with that amount $amount") {
}