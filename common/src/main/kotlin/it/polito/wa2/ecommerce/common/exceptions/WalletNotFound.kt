package it.polito.wa2.ecommerce.common.exceptions

class WalletNotFound(id:String) : NotFoundException("Cannot find wallet with id $id") {
    constructor(id:Long): this(id.toString())

}
