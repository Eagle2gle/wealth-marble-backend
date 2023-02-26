## kafka docker 설정

```shell
$ docker-compose -f docker-compose.yml up -d
$ docker-compose exec kafka kafka-topics --create --topic kafka-market --bootstrap-server kafka:9092 --replication-factor 1 --partitions 1
$ docker-compose exec kafka kafka-topics --describe --topic kafka-market --bootstrap-server kafka:9092
```
1 line : docker-compose 실행

2 line : topic 없을 시, topic (kafka-market) 생성 -> docker-compose.yml에 `KAFKA_CREATE_TOPICS` 가 없는 경우

3 line : kafka-market topic이 있는지 확인

## network 설정
```shell
$ docker network create wm-net # wm-net이라는 이름으로 network 생성
$ docker network ls # network 목록
$ docker network inspect wm-net # wm-net 상세 정보
$ docker network conenct 네트워크명 컨테이너명 # 컨테이너를 네트워크로 연결
$ docker network disconnet 네트워크명 컨테이너명
$ docker run -itd --name two --network wm-net busybox # container 실행 시 네트워크 연결
```