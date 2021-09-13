#!/bin/sh

parameters_num=1
allowed_parameters=("catalogue" "mail" "order" "user" "wallet" "warehouse")

if [ $# != $parameters_num ]; then
  echo "You gave $# parameters instead of $parameters_num"
  exit 1
fi

flag=false
for allowed_elem in ${allowed_parameters[@]}; do
  if [ $allowed_elem == $1 ]; then
    flag=true
    break
  fi
done

if [ $flag == false ]; then
  echo "Allowed parameters: ${allowed_parameters[@]}"
  exit 2
fi

service=$1

if [ "$service" = "catalogue" ]; then
  ./gradlew :$service-service:bootJar
 else
  ./gradlew :$service-service:$service-server:bootJar
fi
docker rm -f $service-service
docker-compose build $service-service
docker-compose up -d $service-service