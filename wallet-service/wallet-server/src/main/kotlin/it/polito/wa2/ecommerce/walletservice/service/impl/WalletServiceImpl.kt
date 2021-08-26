package it.polito.wa2.ecommerce.walletservice.service.impl

import it.polito.wa2.ecommerce.common.exceptions.BadRequestException
import it.polito.wa2.ecommerce.common.exceptions.WalletNotFound
import it.polito.wa2.ecommerce.common.parseID
import it.polito.wa2.ecommerce.walletservice.client.wallet.WalletDTO
import it.polito.wa2.ecommerce.walletservice.client.wallet.request.CustomerWalletCreationRequestDTO
import it.polito.wa2.ecommerce.walletservice.client.wallet.request.WalletCreationRequestDTO
import it.polito.wa2.ecommerce.walletservice.client.wallet.request.WarehouseWalletCreationRequestDTO
import it.polito.wa2.ecommerce.walletservice.domain.Wallet
import it.polito.wa2.ecommerce.walletservice.domain.WalletType
import it.polito.wa2.ecommerce.walletservice.repository.WalletRepository
import it.polito.wa2.ecommerce.walletservice.service.WalletService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
@Transactional
class WalletServiceImpl : WalletService {

    @Autowired
    lateinit var walletRepository: WalletRepository


    override fun getWalletById(id: String): WalletDTO {
        val idInt = id.parseID()
        return walletRepository.findByIdOrNull(idInt)?.toDTO() ?: throw WalletNotFound(id)
    }

    override fun addWallet(walletCreationRequest: WalletCreationRequestDTO): WalletDTO {
        //TODO add check if userID == principal.userID  or is admin
        val wallet = walletCreationRequest.toEntity()

        if (walletCreationRequest is WarehouseWalletCreationRequestDTO &&
            walletRepository.findByWalletTypeAndOwner(WalletType.WAREHOUSE, walletCreationRequest.warehouseID) != null
        )
            throw BadRequestException("Warehouses cannot have more than one wallet")

        return walletRepository.save(wallet).toDTO()
    }

    override fun deleteWallet(walletId: String) {
        val id = walletId.parseID()
        //TODO verify that is the owner (or admin to delete the wallet)
        walletRepository.deleteById(id)
    }


    override fun getWalletOrThrowException(walletId: Long): Wallet {
        return walletRepository.findByIdOrNull(walletId) ?: throw WalletNotFound(walletId)
    }


}

fun WalletCreationRequestDTO.toEntity(): Wallet {
    return when (this) {
        is WarehouseWalletCreationRequestDTO -> Wallet(this.warehouseID, WalletType.WAREHOUSE)
        is CustomerWalletCreationRequestDTO -> Wallet(this.customerID, WalletType.CUSTOMER)
        else -> throw BadRequestException("Request type is not recognised")
    }
}