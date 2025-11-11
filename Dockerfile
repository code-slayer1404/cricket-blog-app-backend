# Use official Java 17 image
FROM eclipse-temurin:17-jdk

# Set working directory inside container
WORKDIR /app

# Copy everything into the container
COPY . /app

# Build the app and skip tests
RUN ./mvnw package -DskipTests

# Run the actual jar file
CMD ["java", "-jar", "target/blog-app-0.0.1-SNAPSHOT.jar"]