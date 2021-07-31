rootProject.name = "ecommerce"

include(":wallet-service:wallet-client", ":wallet-service:wallet-server",
    ":warehouse-service:warehouse-client", ":warehouse-service:warehouse-server",
    ":order-service:order-client", ":order-service:order-server",
    ":user-service:user-client", ":user-service:user-server",
    ":mail-service:mail-client", ":mail-service:mail-server",
    ":catalogue-service", ":common"
    )