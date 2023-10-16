FROM openjdk:17
COPY target/ravtech*.jar /usr/src/ravtech.jar
COPY src/main/resources/application.properties /opt/conf/application.properties
CMD ["java", "-jar", "/usr/src/ravtech.jar", "--spring.config.location=file:/opt/conf/application.properties"]
