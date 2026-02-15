FROM eclipse-temurin:25
RUN mkdir /opt/app
WORKDIR /opt/app
COPY ./ssg-0.0.1-SNAPSHOT.jar .
COPY ./application.properties .
ENV PORT=3000
CMD ["java", "-jar", "ssg-0.0.1-SNAPSHOT.jar", "spring.config.location=file:/opt/app/application.properties"]