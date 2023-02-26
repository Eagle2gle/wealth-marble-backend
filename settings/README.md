## kafka docker 설정

```shell
$ docker-compose -f docker-compose.yml up -d
$ docker-compose exec kafka kafka-topics --create --topic kafka-market --bootstrap-server kafka:9092 --replication-factor 1 --partitions 1
$ docker-compose exec kafka kafka-topics --describe --topic kafka-market --bootstrap-server kafka:9092
```
1 line : docker-compose 실행

2 line : topic 없을 시, topic (kafka-market) 생성 -> docker-compose.yml에 `KAFKA_CREATE_TOPICS` 가 없는 경우

3 line : kafka-market topic이 있는지 확인

## Redis docker 설정

```shell
docker pull redis:alpine
docker run --name redis -p 6379:6379 -d redis
```