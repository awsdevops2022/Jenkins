
FROM ubuntu AS checkout
RUN apt-get update -y \
 && apt-get upgrade -y \
 && apt-get install -y git \
 && git clone https://github.com/saikrishnna29/mavenrepo.git 

FROM maven:3.8.5-jdk-8 AS package
ARG USER
RUN mkdir -p package && cd package  
COPY --from=checkout /mavenrepo/src .
COPY --from=checkout /mavenrepo/pom.xml .
RUN mvn package && echo "Hello $USER" 
