
# Define a imagem base
FROM openjdk:8 as package

# Define o diretório de trabalho dentro do contêiner
WORKDIR /app

# Copia o arquivo JAR para o diretório de trabalho
COPY target/bill-manager-0.0.1-SNAPSHOT.jar app.jar

# Define o comando para executar o aplicativo quando o contêiner for iniciado
CMD ["java", "-jar", "app.jar"]


# Etapa de CONSTRUÇÃO
#FROM maven:alpine AS build
#WORKDIR /home/app

# Criar o diretório src/main (caso não exista)
# RUN mkdir -p src/main

# Copiar os arquivos do projeto
#COPY /src/main /home/app/src/main
#COPY /src/main /home/app/
#COPY pom.xml /home/app

# Compilar o projeto
#RUN mvn clean package 

# Etapa de EMPACOTAMENTO

#FROM openjdk:8 as package
#COPY --from=build /home/app/target/bill-manager-0.0.1-SNAPSHOT.jar /home/app/bill-manager-0.0.1-SNAPSHOT.jar

#EXPOSE 8080
#ENTRYPOINT ["java","-jar","/home/app/bill-manager-0.0.1-SNAPSHOT.jar", "--spring.main.main-class=com.ajudaqui.bill.manager.BillManagerApplication"]
