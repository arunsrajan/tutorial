# Define the number of replicas
REPLICAS=2

docker node update --label-add nodetype=namenode vm1
docker node update --label-add nodetype=datanodes vm2
docker node update --label-add nodetype=datenodes vm3

# Define the base directory for volumes
BASE_DIR=/vagrant/datanodes

# Create a unique source directory for each container
for ((container_id=1; container_id<=$REPLICAS; container_id=container_id+1))
do
    # Generate unique source directory name
    UNIQUE_SOURCE=datanode$container_id

    # Create the directory on the host
    mkdir -p $BASE_DIR/$UNIQUE_SOURCE
	
	echo docker service create --mount type=bind,source=$BASE_DIR/$UNIQUE_SOURCE,target=/hadoop-data --network hadoopnetwork --name datanode$container_id -e "HDFS_CONF_dfs_datanode_data_dir=/hadoop-data" -e "HDFS_CONF_dfs_namenode_rpc___address=namenodehost:8020" -e "HDFS_CONF_dfs_namenode_servicerpc___address=namenodehost:8040" -e "HDFS_CONF_dfs_namenode_datanode_registration_ip___hostname___check=false" --constraint node.labels.nodetype==datanodes bde2020/hadoop-datanode:latest

    # Create the service with bind mount
	docker service create --mount type=bind,source=$BASE_DIR/$UNIQUE_SOURCE,target=/hadoop-data --network hadoopnetwork --name datanode$container_id -e "HDFS_CONF_dfs_datanode_data_dir=/hadoop-data" -e "HDFS_CONF_dfs_namenode_rpc___address=namenodehost:8020" -e "HDFS_CONF_dfs_namenode_servicerpc___address=namenodehost:8040" -e "HDFS_CONF_dfs_namenode_datanode_registration_ip___hostname___check=false" --constraint node.labels.nodetype==datanodes bde2020/hadoop-datanode:latest
	
done
