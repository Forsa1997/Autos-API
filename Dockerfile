# Uses Open JDK as base image, the tag '8-jdk-alpine' denotes the "version" we are using
FROM openjdk:8-jdk-alpine

# Set the working directory inside the container when run, here it is root
WORKDIR /

# Take the jar from the build folder and add it as hello-world.jar. This will require there to be a build already in that directory. Please modify the first path to your needs.
COPY build/libs/autos-0.0.1-SNAPSHOT.jar autos.jar

# Remember how we mapped container ports to our host's port? This is how you expose the port you wish. It exposes PORT 8080.
EXPOSE 8080

# Invoke java executable and run the hello-world.jar file. There is only ONE CMD instruction in a Dockerfile and it is used as default to executing the container. The CMD form can vary, refer to the Docker Docs: Dockerfile Reference on formatting these shell commands.
CMD java -jar autos.jar