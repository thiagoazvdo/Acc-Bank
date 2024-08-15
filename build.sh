#!/bin/bash

mvn clean package spring-boot:repackage -DskipTests
docker rmi accbank-api
docker build -t accbank-api .
