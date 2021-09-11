#!/bin/sh

set -e

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

docker-compose down
docker-compose build
docker-compose up -d

until docker exec -i mysql sh -c "exec mysql -uroot --password=admin " < ./databases/init.sql
  do
    sleep 5
  done

# Debezium configuration
DEBEZIUM_CONFIG_FILE='./debezium_config.json'
DEBEZIUM_CONFIG="$(cat "$DEBEZIUM_CONFIG_FILE" | tr -d '\r' | tr -d '\n' | tr -d ' ' )"

until output=$(docker exec kafka-connect curl -f kafka-connect:8083/connectors)
  do
    echo "Service is unavailable, sleeping..."
    sleep 5
  done

#  example: $output =  ["test-router","test-router-2"]

#  example: $output =  test-router,test-router-2
output=$(echo $output | tr -d '[' | tr -d ']' | tr -d '"')

# example: $output[0] = test-router | $output[1] = test-router-2
IFS=',' read -a output <<< "$output"

for connector in ${output[@]}
do
  until docker exec kafka-connect curl -i -f -X DELETE "kafka-connect:8083/connectors/""$connector"
    do
      echo "Route is unavailable, sleeping..."
      sleep 5
    done
done

until docker exec kafka-connect curl -i -f -d "$DEBEZIUM_CONFIG" -H "Content-Type:application/json" -X POST kafka-connect:8083/connectors
    do
      echo "Route is unavailable, sleeping..."
      sleep 5
    done