#!/bin/sh

# if errors appear, run ecommerce->build setup->wrapper

echo " "
echo " "
echo 'starting gradle tasks...'
echo " "
echo " "

echo " "
echo 'Eureka server'
#./gradlew :eureka-server:build
./gradlew :eureka-server:bootJar

echo " "
echo 'Catalogue service'
#./gradlew :catalogue-service:build
./gradlew :catalogue-service:bootJar

# TODO
# Check if my changes also work in your configurations
# With the previous commands, this script couldn't find gradlew files
# nested inside x-server folders

echo " "
echo 'Mail service'
#./gradlew :mail-service:mail-server:build
./gradlew :mail-service:mail-server:bootJar

echo " "
echo 'Order service'
#./gradlew :order-service:order-server:build
./gradlew :order-service:order-server:bootJar

echo " "
echo 'User service'
#./gradlew :user-service:user-server:build
./gradlew :user-service:user-server:bootJar

echo " "
echo 'Wallet service'
#./gradlew :wallet-service:wallet-server:build
./gradlew :wallet-service:wallet-server:bootJar

echo " "
echo 'Warehouse service'
#./gradlew :warehouse-service:warehouse-server:build
./gradlew :warehouse-service:warehouse-server:bootJar

echo " "
echo " "
echo 'starting docker-compose...'
echo " "
echo " "
docker-compose build
docker-compose up -d


docker exec -i mysql sh -c "exec mysql -uroot --password=admin " < ./databases/init.sql

# Configure debezium
DEBEZIUM_CONFIG_FILE="./sample_debezium_config.json"
DEBEZIUM_CONFIG="$(cat "$DEBEZIUM_CONFIG_FILE" | tr -d '\r' | tr -d '\n')"
#echo "$DEBEZIUM_CONFIG" > TEST_FILE.txt


# TODO update "database.include.list" and "table.include.list"
echo " "
docker exec kafka-connect curl -d "$DEBEZIUM_CONFIG" -H "Content-Type: application/json" -X POST kafka-connect:8083/connectors