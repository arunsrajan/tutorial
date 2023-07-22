#!/bin/bash

REPLICAS=${REPLICAS:-3}  # Number of replicas, defaulting to 3
REPLICAS_MANAGEMENT=${REPLICAS_MANAGEMENT:-1}  # Number of replicas, defaulting to 3
DATA_DIR="/var/lib/mysql"  # Specify the path to the data directory

# Generate my.cnf
cat <<EOF > my.cnf
[mysqld]
ndbcluster
ndb-connectstring=10.0.1.200
datadir=${DATA_DIR}

[mysql_cluster]
ndb-connectstring=10.0.1.200

[ndbd]
connect-string=10.0.1.200

[ndb_mgm]
connect-string=10.0.1.200
EOF

# Generate mysql-cluster.cnf
cat <<EOF > mysql-cluster.cnf
[ndbd default]
noofreplicas=${REPLICAS}
datadir=${DATA_DIR}

[ndb_mgmd]
NodeId=$REPLICAS_MANAGEMENT
datadir=${DATA_DIR}
hostname=10.0.1.200

[mysqld]
NodeId=$(($REPLICAS+$REPLICAS_MANAGEMENT+1))
hostname=mysql1

EOF

for ((i=$REPLICAS_MANAGEMENT+1; i<=$REPLICAS+$REPLICAS_MANAGEMENT; i++)); do
    cat <<EOF >> mysql-cluster.cnf
[ndbd]
NodeId=${i}
hostname=10.0.1.$((${i}-$REPLICAS_MANAGEMENT+10))
datadir=${DATA_DIR}

EOF
done

cat <<EOF > ./management/config/config.ini
[ndb_mgmd default] 
DataDir=/var/lib/mysql
    
[ndbd default]
DataDir=/var/lib/mysql
DataMemory=256M
NoOfReplicas=${REPLICAS}
    
[tcp default]
AllowUnresolvedHostnames=1
    
[ndb_mgmd]
NodeId=1
Hostname=10.0.1.200

EOF

for i in $(seq 1 $REPLICAS); do
cat <<EOF >> ./management/config/config.ini
[ndbd]
NodeId=$((${i}+$REPLICAS_MANAGEMENT))
Hostname=10.0.1.$((${i}+10))

EOF
done

cat <<EOF >> ./management/config/config.ini
[mysqld]
Hostname=mysql${REPLICAS_MANAGEMENT}

EOF

PROJECT_NAME="ndb_cluster"

for i in $(seq 1 $REPLICAS); do
  VOLUME_NAME="${PROJECT_NAME}_data_${i}"
 docker volume create --driver local --opt type=none --opt device=/vagrant/data${i} --opt o=bind $VOLUME_NAME
done
docker volume create --driver local --opt type=none --opt device=/vagrant/management --opt o=bind management

export DOCKER_INSTANCE_INDEX=2
#docker-compose up -d
