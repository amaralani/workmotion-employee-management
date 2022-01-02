FROM adoptopenjdk/openjdk11:jdk-11.0.13_8-alpine-slim AS buildfile

ADD . /build
WORKDIR /build
RUN ./mvnw -B clean package

FROM adoptopenjdk/openjdk11:jdk-11.0.13_8-alpine-slim
COPY --from=buildfile /build/target/employee-management-0.0.1.jar app.jar
RUN sh -c 'touch /app.jar'
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app.jar"]