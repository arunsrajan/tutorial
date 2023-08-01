FROM openjdk:17
RUN mkdir -p /opt/springboot/data
RUN mkdir -p /opt/springboot/config
COPY config/application.properties /opt/springboot/config/application.properties
COPY target/springbootcamel.jar /opt/springboot/
WORKDIR /opt/springboot/
RUN cd /opt/springboot/
EXPOSE 1090
EXPOSE 8080
CMD ["java","-jar","springbootcamel.jar","--spring.config.location=file:config/application.properties"]
