package it.polito.wa2.ecommerce.walletservice.service.impl

import it.polito.wa2.ecommerce.common.Rolename
import it.polito.wa2.ecommerce.common.exceptions.BadRequestException
import it.polito.wa2.ecommerce.common.exceptions.ForbiddenException
import it.polito.wa2.ecommerce.common.getPageable
import it.polito.wa2.ecommerce.common.parseID
import it.polito.wa2.ecommerce.common.security.IdentityVerifier
import it.polito.wa2.ecommerce.common.security.JwtTokenDetails
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
import org.springframework.security.core.context.SecurityContextHolder
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


    override fun getWalletOrThrowException(walletId: Long, verifySecurity: Boolean): Wallet {
        val wallet = walletRepository.findByIdOrNull(walletId) ?: throw WalletNotFound(walletId)
        if (verifySecurity)
            identityVerifier.verifyUserIdentityOrIsAdmin(wallet.owner.parseID())
        return wallet
    }

    override fun getAllWallets(ownerId: String?, walletType: String?, pageIdx: Int, pageSize: Int): List<WalletDTO> {
        val page = getPageable(pageIdx, pageSize)
        val principal = SecurityContextHolder.getContext().authentication.principal as JwtTokenDetails
        if(principal.roles.contains(Rolename.ADMIN)){
            var type :WalletType? = null
            walletType?.let{
                try {
                    type =  WalletType.valueOf(it)
                }catch (i:IllegalArgumentException){
                    throw BadRequestException("Invalid wallet type $walletType")
                }
            }

            return walletRepository.findByOwnerAndWalletType(ownerId, type, page).map{it.toDTO()}.toList()

        }else{
            if(walletType!=null || ownerId!=null){
                throw ForbiddenException("Cannot access other wallets")
            }
            return walletRepository.findByOwnerAndWalletType(principal.id,WalletType.CUSTOMER, page).map { it.toDTO() }.toList()
        }
    }


}

fun WalletCreationRequestDTO.toEntity(): Wallet {
    return when (this) {
        is WarehouseWalletCreationRequestDTO -> Wallet(this.warehouseID, WalletType.WAREHOUSE)
        is CustomerWalletCreationRequestDTO -> Wallet(this.customerID, WalletType.CUSTOMER)
        else -> throw BadRequestException("Request type is not recognised")
    }
}