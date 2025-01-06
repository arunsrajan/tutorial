FROM openjdk:21-jdk-slim
USER root
ADD apache-artemis-2.37.0-bin.tar.gz /var/lib
RUN mv /var/lib/apache-artemis-2.37.0 /var/lib/artemis
RUN apt-get update && apt-get install -y curl
WORKDIR /var/lib/artemis
RUN bin/artemis create --user admin --password admin --relax-jolokia --require-login --host 0.0.0.0 --http-host 0.0.0.0 ./artemis-instance
EXPOSE 8161 8162 61616 61617
ENTRYPOINT ["./artemis-instance/bin/artemis","run"]
