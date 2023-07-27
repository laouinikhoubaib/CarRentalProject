FROM openjdk:11
EXPOSE 8080
ADD target/springprojet.jar springprojet.jar
ENTRYPOINT ["java","-jar","/springprojet.jar"]
