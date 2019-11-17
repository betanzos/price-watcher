FROM adoptopenjdk:11-jre-openj9

LABEL mantainer="Eduardo Betanzos <ebetanzos@hotmail.es>"

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/price-watcher.jar", "--root.dir=/mnt/videos"]

ARG JAR_FILE
ADD $JAR_FILE /price-watcher.jar