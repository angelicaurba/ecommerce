package it.polito.wa2.ecommerce.common.exceptions

class TransactionNotFoundException(msg:String) : NotFoundException(msg){
    constructor(id:Long): this("Cannot found transaction with id $id")
}
