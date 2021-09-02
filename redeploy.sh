#!/bin/sh

# if errors appear, run ecommerce->build setup->wrapper

echo " "
echo " "
echo 'starting gradle tasks...'
echo " "
echo " "

#./gradlew :eureka-server:build
./gradlew :eureka-server:bootJar

#./gradlew :catalogue-service:build
./gradlew :catalogue-service:bootJar

#./gradlew :mail-service:build
./gradlew :mail-service:bootJar

#./gradlew :order-service:build
./gradlew :order-service:bootJar

#./gradlew :user-service:build
./gradlew :user-service:bootJar

#./gradlew :wallet-service:build
./gradlew :wallet-service:bootJar

#./gradlew :wallet-service:build
./gradlew :wallet-service:bootJar

echo " "
echo " "
echo 'starting docker-compose...'
echo " "
echo " "
docker-compose build
docker-compose up -d