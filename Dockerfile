FROM openjdk:21

WORKDIR /app

COPY . /app

RUN ./mvnw -B clean package

EXPOSE 8082

CMD ["java", "-jar", "target/api-clientes-0.0.1-SNAPSHOT.jar"]