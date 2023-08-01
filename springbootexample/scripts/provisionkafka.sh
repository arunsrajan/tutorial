docker run -d --name zookeeper --network redis_redisnet -e ALLOW_ANONYMOUS_LOGIN=yes bitnami/zookeeper:latest
docker run -d --network redis_redisnet --name kafka -e ALLOW_PLAINTEXT_LISTENER=yes -e KAFKA_CFG_ZOOKEEPER_CONNECT=zookeeper:2181 bitnami/kafka:latest
docker run --name activemq --network redis_redisnet -p 61616:61616 -p 8161:8161 -d rmohr/activemq
docker run --network redis_redisnet --name springboot1 -P -v "C:/DEVELOPMENT:/DEVELOPMENT" -d springbootcamel
docker run --network redis_redisnet --name springboot2 -P -v "C:/DEVELOPMENT:/DEVELOPMENT" -d springbootcamel
docker exec -it redis-redis1-1 redis-cli --cluster create redis1:6379 redis2:6379 redis3:6379 redis4:6379 redis5:6379 redis6:6379 --cluster-replicas 1 --cluster-yes