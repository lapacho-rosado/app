FROM maven:3.6.3-openjdk-8-slim
LABEL mantainer="tmarmoni@ambiente.gob.ar"
ENV TZ=America/Argentina/Buenos_Aires
ENV LANG=es_AR.UTF-8

WORKDIR /app/source
COPY pom.xml pom.xml
# COPY lib lib
RUN mvn dependency:go-offline

COPY src src
ARG CGL_DOMINIO
ENV CGL_DOMINIO=$CGL_DOMINIO
RUN mkdir -p /app/builds \
    && sed -i "s@Server=.*@Server=${CGL_DOMINIO}@" ./src/main/resources/Config.properties \
    && mvn -e -B package

CMD /bin/bash -c "cp -f target/*.war /app/builds/app.war"
