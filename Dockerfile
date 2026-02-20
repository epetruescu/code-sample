FROM public.ecr.aws/amazoncorretto/amazoncorretto:17-al2023 AS build
WORKDIR /app
RUN dnf install -y findutils && dnf clean all
COPY . .

# List directories to see what was copied
RUN ls -la && echo "=== gradle dir ===" && ls -la gradle/ && echo "=== src dir ===" && ls -la src/

RUN chmod +x gradlew
RUN ./gradlew bootJar --no-daemon

FROM public.ecr.aws/amazoncorretto/amazoncorretto:17-al2023
WORKDIR /app

COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]