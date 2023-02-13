## kafka docker 설정

```shell
$ docker-compose -f docker-compose.yml up -d
$ docker-compose exec kafka kafka-topics --create --topic kafka-market --bootstrap-server kafka:9092 --replication-factor 1 --partitions 1
$ docker-compose exec kafka kafka-topics --describe --topic kafka-market --bootstrap-server kafka:9092
```
1 line : docker-compose 실행

docker compose에 KAFKA_CREATE_TOPICS가 있으면 별도 topic 생성하지 않아도 된다.

2 line : topic (kafka-market) 생성

3 line : kafka-market topic이 있는지 확인

