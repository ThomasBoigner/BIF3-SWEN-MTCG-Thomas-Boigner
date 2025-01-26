FROM openjdk:23
ARG JAR_FILE=target/BIF3-SWEN-MTCG-Thomas-Boigner.jar
COPY target/BIF3-SWEN-MTCG-Thomas-Boigner.jar mtcg.jar
ENTRYPOINT ["java", "-jar", "/mtcg.jar"]