FROM adoptopenjdk/openjdk11:latest
RUN useradd --create-home reactive-user
USER reactive-user
WORKDIR /home/reactive-user
ARG JAR_FILE=build/libs/\*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/home/reactive-user/app.jar"]