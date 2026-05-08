Done.                                                                                                                                                                                           
Service [travel-engine] revision [travel-engine-00005-r2b] has been deployed and is serving 100 percent of traffic.
Service URL: https://travel-engine-714692079078.us-central1.run.app
shashwatsrivastava0502@cloudshell:~/travel-engine (linen-flux-495706-e5)$ gcloud logs tail --project linen-flux-495706-e5
ERROR: (gcloud) Invalid choice: 'logs'.
Maybe you meant:
  gcloud app logs tail
  gcloud network-management vpc-flow-logs-configs query-org-vpc-flow-logs-configs
  gcloud network-management vpc-flow-logs-configs show-effective-flow-logs-configs

To search the help text of gcloud commands, run:
  gcloud help -- SEARCH_TERMS# Stage 1: Build the Angular frontend
FROM node:20 AS frontend-build
WORKDIR /app/frontend
COPY frontend/package*.json ./
RUN npm install
COPY frontend/ ./
# Add Google Maps types fix during build
RUN npm install --save-dev @types/google.maps
# Update tsconfig to include google.maps types
RUN sed -i 's/"types": \[\]/"types": ["google.maps"]/' tsconfig.app.json
RUN npm run build

# Stage 2: Build the Spring Boot backend
FROM maven:3.9-eclipse-temurin-17 AS backend-build
WORKDIR /app
COPY pom.xml .
COPY src ./src
# Create resources/static directory
RUN mkdir -p src/main/resources/static
# Copy the built frontend to Spring Boot static resources
COPY --from=frontend-build /app/frontend/dist/frontend/browser/ src/main/resources/static/
RUN mvn clean package -DskipTests

# Stage 3: Run the application
FROM eclipse-temurin:17-jre-jammy
COPY --from=backend-build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
