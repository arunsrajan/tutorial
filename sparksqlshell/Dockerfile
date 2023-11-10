FROM brainlounge/jumppod:ubuntu-18.04

RUN apt update && apt install -y apt-transport-https apt-utils
RUN curl -s https://packages.cloud.google.com/apt/doc/apt-key.gpg | apt-key add - echo "deb https://apt.kubernetes.io/ kubernetes-xenial main" | tee -a /etc/apt/sources.list.d/kubernetes.list
RUN apt update
RUN apt install -y kubectl openjdk-17-jdk apt-transport-https apt-utils
ADD spark-3.5.0-bin-hadoop3.tgz /opt
RUN ln -s /opt/spark-3.5.0-bin-hadoop3 /opt/spark