FROM apache/spark:3.5.1-scala2.12-java11-python3-r-ubuntu
USER root
COPY aws-java-sdk-bundle-1.12.276.jar /opt/spark/jars
COPY hadoop-aws-3.3.4.jar /opt/spark/jars
WORKDIR /app
COPY main.py .
