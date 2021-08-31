package it.polito.wa2.ecommerce.walletservice.exception

import it.polito.wa2.ecommerce.common.exceptions.NotFoundException

class TransactionNotFoundException(msg:String) : NotFoundException(msg){
    constructor(id:Long): this("Cannot found transaction with id $id")
}
