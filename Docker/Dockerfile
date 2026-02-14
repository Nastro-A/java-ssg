FROM eclipse-temurin:25
RUN mkdir /opt/app
WORKDIR /opt/app
COPY ../target/ssg-0.0.1-SNAPSHOT.jar .
ENV PORT=3000
CMD ["java", "-jar", "ssg-0.0.1-SNAPSHOT.jar"]