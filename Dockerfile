FROM openjdk:11
VOLUME /tmp
EXPOSE 8080/tcp
ARG JAVA_OPTS
ENV JAVA_OPTS=$JAVA_OPTS
COPY target/usuario-0.0.1-SNAPSHOT.jar usuario.jar
ENTRYPOINT exec java $JAVA_OPTS -jar usuario.jar
# For Spring-Boot project, use the entrypoint below to reduce Tomcat startup time.
#ENTRYPOINT exec java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar usuario.jar