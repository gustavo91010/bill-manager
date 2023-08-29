#!/bin/bash

# echo "opa !"

mvn clean package install &&       docker build -t bill-manager_api . &&     docker run -p 8080:8080 bill-manager_api:latest
           

