# Use official Java 17 image
FROM eclipse-temurin:17-jdk

# Set working directory inside container
WORKDIR /app

# Copy everything into the container
COPY . /app

# # Build the app using Maven wrapper
# RUN ./mvnw package
RUN ./mvnw package -DskipTests

# Run the jar file
CMD ["java", "-jar", "target/cricket-blog-app-backend.jar"]