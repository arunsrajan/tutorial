FROM quay.io/strimzi/kafka:0.43.0-kafka-3.7.0
USER root
# Add additional plugins or configurations here
ADD kafka-connect-jms/confluentinc-kafka-connect-activemq-12.2.5 /opt/kafka/plugins/confluentinc-kafka-connect-activemq-12.2.5
ADD kafka-connect-jms/confluentinc-kafka-connect-jms-12.2.5 /opt/kafka/plugins/confluentinc-kafka-connect-jms-12.2.5
COPY connectors/*.jar /opt/kafka/plugins
USER 1001
