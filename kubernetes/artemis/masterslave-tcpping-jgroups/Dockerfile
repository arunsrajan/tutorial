FROM openjdk:21-jdk-slim
USER root
ADD apache-artemis-2.37.0-bin.tar.gz /var/lib
RUN mv /var/lib/apache-artemis-2.37.0 /var/lib/artemis
WORKDIR /var/lib/artemis
RUN bin/artemis create --user admin --password admin --relax-jolokia --require-login --host 0.0.0.0 --http-host 0.0.0.0 ./artemis-instance
EXPOSE 8161 61616
ENTRYPOINT ["./artemis-instance/bin/artemis","run"]