FROM quay.io/strimzi/kafka:0.43.0-kafka-3.7.0
USER root
# Add additional plugins or configurations here
COPY connectors/*.jar /usr/share/java
COPY connectors/*.jar /opt/kafka/plugins
