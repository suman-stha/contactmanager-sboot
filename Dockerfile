FROM openjdk:8
EXPOSE 8080
ADD target/smartcontactmanager-docker.jar smartcontactmanager-docker.jar
ENTRYPOINT [ "java","-jar","/smartcontactmanager-docker.jar" ]