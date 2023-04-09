FROM openjdk:17-slim

RUN apt-get update -y \
  && apt-get -y install gettext-base curl telnet ca-certificates

WORKDIR /app

COPY core/target/*.jar /app/app.jar
COPY core/config /app/config

EXPOSE 8080/tcp

# Add entrypoint.sh script
ADD devops/k8s/core/entrypoint.sh /app/entrypoint.sh

RUN chmod +x /app/entrypoint.sh

ENTRYPOINT ["/app/entrypoint.sh"]
