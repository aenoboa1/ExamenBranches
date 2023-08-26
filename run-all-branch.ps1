# Build the Spring Boot application using Maven
mvn clean package
# build image
docker build -t branches:latest .
# Run the Docker container
docker run --rm --name branches -it --network dockerNetwork -p 8080:8080 branches:latest

