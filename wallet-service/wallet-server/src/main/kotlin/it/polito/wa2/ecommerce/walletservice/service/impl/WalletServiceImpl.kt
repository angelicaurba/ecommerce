package it.polito.wa2.ecommerce.walletservice.service.impl

import it.polito.wa2.ecommerce.common.exceptions.BadRequestException
import it.polito.wa2.ecommerce.common.parseID
import it.polito.wa2.ecommerce.common.security.IdentityVerifier
import it.polito.wa2.ecommerce.walletservice.client.wallet.WalletDTO
import it.polito.wa2.ecommerce.walletservice.client.wallet.request.CustomerWalletCreationRequestDTO
import it.polito.wa2.ecommerce.walletservice.client.wallet.request.WalletCreationRequestDTO
import it.polito.wa2.ecommerce.walletservice.client.wallet.request.WarehouseWalletCreationRequestDTO
import it.polito.wa2.ecommerce.walletservice.domain.Wallet
import it.polito.wa2.ecommerce.walletservice.domain.WalletType
import it.polito.wa2.ecommerce.walletservice.exception.WalletNotFound
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

    @Autowired
    lateinit var identityVerifier: IdentityVerifier

    override fun getWalletById(id: String): WalletDTO {
        return getWalletOrThrowException(id.parseID()).toDTO()
    }

    override fun addWallet(walletCreationRequest: WalletCreationRequestDTO, verifySecurity:Boolean): WalletDTO {
        val wallet = walletCreationRequest.toEntity()

        if (walletCreationRequest is WarehouseWalletCreationRequestDTO) {
            if (verifySecurity)
                identityVerifier.verifyIsAdmin()
            if (walletRepository.findByWalletTypeAndOwner(
                    WalletType.WAREHOUSE,
                    walletCreationRequest.warehouseID
                ) != null
            )
                throw BadRequestException("Warehouses cannot have more than one wallet")

        } else {
            //Customer wallet
            if(verifySecurity)
                identityVerifier.verifyUserIdentityOrIsAdmin(wallet.owner.parseID())
        }
        return walletRepository.save(wallet).toDTO()
    }


    override fun getWalletOrThrowException(walletId: Long): Wallet {
        //TODO is it ok to throw notFound before ownership check?
        val wallet = walletRepository.findByIdOrNull(walletId) ?: throw WalletNotFound(walletId)
        identityVerifier.verifyUserIdentityOrIsAdmin(wallet.owner.parseID())
        return wallet
    }


}

fun WalletCreationRequestDTO.toEntity(): Wallet {
    return when (this) {
        is WarehouseWalletCreationRequestDTO -> Wallet(this.warehouseID, WalletType.WAREHOUSE)
        is CustomerWalletCreationRequestDTO -> Wallet(this.customerID, WalletType.CUSTOMER)
        else -> throw BadRequestException("Request type is not recognised")
    }
}