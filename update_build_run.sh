#!/bin/bash

# echo "opa !"

mvn clean package install &&       docker build -t bill-manager_api . &&     docker run -p 8080:8080 bill-manager_api:latest
           

# docker compose up faz a parada toda rodar, com bando  independente
