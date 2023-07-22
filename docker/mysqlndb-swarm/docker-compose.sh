#!/bin/bash

REPLICAS=3
REPLICAS_MANAGEMENT=${REPLICAS_MANAGEMENT:-1}  # Number of replicas, defaulting to 3
VOLUME_NAME_PREFIX="ndb_cluster_data"

# Generate Docker Compose file dynamically
cat <<EOF > docker-compose.yaml
version: "3.7"

services:
EOF
cat <<EOF >> docker-compose.yaml
  management:
    ports:
      - "1186:1186"
    networks:
      mysqlswarm:
        ipv4_address: 10.0.1.200
    hostname: 10.0.1.200
    image: mysql/mysql-cluster:8.0.32
    command: ["/usr/sbin/ndb_mgmd","-f","/var/lib/mysql/config/config.ini","--configdir=/var/lib/mysql","--ndb-nodeid=1","--initial","--nodaemon","-v"]
    volumes:
      - management:/var/lib/mysql
      - type: bind
        source: /vagrant/my.cnf
        target: /etc/my.cnf
      - type: bind
        source: /vagrant/mysql-cluster.cnf
        target: /etc/mysql-cluster.cnf
    deploy:
      replicas: 1
      placement:
        constraints:
          - node.hostname == vm1
EOF
for ((i=1; i<=REPLICAS; i++)); do
  volume_name="${VOLUME_NAME_PREFIX}_${i}"
  cat <<EOF >> docker-compose.yaml
  ndb${i}:
    ports:
      - "$(($i+11110)):11110"
    networks:
      mysqlswarm:
        ipv4_address: 10.0.1.$(($i+10))
    hostname: 10.0.1.$(($i+10))
    image: mysql/mysql-cluster:8.0.32
    command: ["/usr/sbin/ndbd","--ndb-nodeid=$(($i+$REPLICAS_MANAGEMENT))","-c","10.0.1.200","--initial","--nodaemon","-v"]
    volumes:
      - ${volume_name}:/var/lib/mysql
      - type: bind
        source: /vagrant/my.cnf
        target: /etc/my.cnf
      - type: bind
        source: /vagrant/mysql-cluster.cnf
        target: /etc/mysql-cluster.cnf
    deploy:
      replicas: 1
      placement:
        constraints:
          - node.hostname == vm${i}
EOF
done
cat <<EOF >> docker-compose.yaml
volumes:
EOF
for ((i=1; i<=REPLICAS; i++)); do
  volume_name="${VOLUME_NAME_PREFIX}_${i}"
  cat <<EOF >> docker-compose.yaml
  ${volume_name}:
    external: true
EOF
done
cat <<EOF >> docker-compose.yaml
  management:
    external: true
networks:
  mysqlswarm:
    name: ndb_network
    external: true
EOF