# build pacakge
#FROM maven:3.3-jdk-8 as builder
#COPY . /usr/src/app
#WORKDIR /usr/src/app
#RUN mvn clean -DskipTests package -B -U -e

# make deploy image
FROM openjdk:8
COPY ./*.jar /usr/src/myapp
WORKDIR /usr/src/myapp
RUN java -jar *.jar