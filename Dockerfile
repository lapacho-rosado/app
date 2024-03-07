FROM maven:3.6.3-openjdk-8-slim
LABEL mantainer="tmarmoni@ambiente.gob.ar"
ENV TZ=America/Argentina/Buenos_Aires
ENV LANG=es_AR.UTF-8

WORKDIR /app/source
COPY pom.xml pom.xml
# COPY lib lib
RUN mvn dependency:go-offline

COPY src src
RUN mkdir -p /app/builds \
    && mvn -e -B clean package

CMD /bin/bash -c "cp -f target/*.war /app/builds/app.war"
