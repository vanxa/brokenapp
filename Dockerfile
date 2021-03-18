FROM openjdk:11
COPY ./build.sh /
RUN /build.sh
COPY ./build/libs/brokenapp-0.0.1.jar /app.jar
WORKDIR /
CMD ["java", "-jar" , "app.jar"]
