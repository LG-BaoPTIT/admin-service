FROM openjdk:8
EXPOSE 9001
ADD target/admin-service.jar admin-service.jar
ENTRYPOINT ["java","-jar","/admin-service.jar"]
