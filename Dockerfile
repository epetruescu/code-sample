FROM public.ecr.aws/amazoncorretto/amazoncorretto:17-al2023 AS build
WORKDIR /app
RUN dnf install -y findutils && dnf clean all
COPY . .
RUN chmod +x gradlew
RUN ./gradlew bootJar --no-daemon

FROM public.ecr.aws/amazoncorretto/amazoncorretto:17-al2023
WORKDIR /app

COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]