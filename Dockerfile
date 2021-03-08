FROM openjdk:8
ADD target/shorturl-2021.3.8.jar shorturl-2021.3.8.jar
EXPOSE 8080
ENTRYPOINT ["java" , "-jar", "shorturl-2021.3.8.jar"]