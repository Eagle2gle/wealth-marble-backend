# "부의마블" 백엔드

## 서비스 소개

> 우리가 어렸을 때 한 번 쯤은 부루마블 게임을 하면서 해외에 땅을 사고 집을 짓고 호텔을 지은 적이 많이 있습니다. 즐겁게 게임했던 그 순간을 현실에서 진행하며 꿈을 이루어 가는 것은 어떨까요? 휴양지에 투자하여 수익도 얻고 내가 원할 때는 휴양도 즐기는 YOLO의 삶을 저희와 함께 이루어가요.

[부의 마블 기획서](https://jinsungone.notion.site/b93a27bdd0f444d9a6e4d0dbe4f6ae0b)

### 배포 🖇

- main : http://wealth-marble.kro.kr/
- dev : http://wealth-marble-dev.kro.kr/
- [팀 Notion](https://jinsungone.notion.site/2d4079df90c6458a9fb72551a73d51c7)

## 주요기능

1. 부동산 공모 등록 기능
    - 해외에 휴양지를 짓고 싶은 사람이 부동산 공모를 등록할 수 있는 기능
2. 부동산 공모 참여 기능
    - 해외 휴양지를 만드는 과정에 참여를 하고 싶은 투자자들이 돈을 넣을 수 있는 기능
3. 지분 거래 기능
    - 해외 휴양지 지분을 가지고 있는 사람들과 일반 사용자들이 공모 후 풀린 지분을 구매하거나 판매 가능

## 팀원

<table>
  <tbody>
    <tr>
        <td align="center"><a href="https://github.com/CEOJINSUNG">김진성</a></td>
        <td align="center"><a href="https://github.com/NaayoungKwon">권나영</a></td>
        <td align="center"><a href="https://github.com/m4nd4r1n">김도형</a></td>
        <td align="center"><a href="https://github.com/YuriKwon">권유리</a></td>
    </tr>
    <tr>
      <td align="center">
        <img src="https://user-images.githubusercontent.com/34162358/224486392-cfdeef7c-4210-4646-a689-caa6a8e07464.jpeg" width="100px;" height="100px;"        </td>
      <td align="center">
        <img src="https://user-images.githubusercontent.com/34162358/224486703-1621bd02-ed4d-4eef-873a-f76078c586be.jpeg" width="100px;" height="100px;" alt="권나영"/>
       </td>
      <td align="center">
        <img src="https://user-images.githubusercontent.com/34162358/224486418-5d615f74-9505-4870-b1ca-9a74fd3ea2a9.png" width="100px;" height="100px;" alt="김도형"/>
      </td>
      <td align="center">
        <img src="https://user-images.githubusercontent.com/34162358/224486435-dc1c67ff-cdc3-46ce-ad92-d25b287b0c99.jpeg" width="100px;" height="100px;" alt="권유리"/>
    </td>
    </tr>
    <tr>
        <td align="center">Backend</td>
        <td align="center">Backend</td>
        <td align="center">Frontend</td>
        <td align="center">Frontend</td>
    </tr>
  </tbody>
</table>


## Backend Infra

### 서버 Architecture

<img width="700" alt="image" src="https://user-images.githubusercontent.com/34162358/224474739-2f6f5e9f-f838-4ff8-bb43-bc747913e8a1.png">

- AWS EC2, RDS, S3를 사용하고 있습니다.
- EC2 인스턴스에서 API, WEBSOCKET, BATCH Docker Container가 구동되고 있습니다.
- Redis는 Master와 Slave를 두고 Slave는 Master의 내용을 snapshot으로 가지고 있습니다.
- Kafka는 zookeeper와 1개의 kafka가 운영되고 있으며 STOMP broker역할을 하고 있습니다.

### 자동 배포 Process

<img width="613" alt="image" src="https://user-images.githubusercontent.com/34162358/224475163-d580267a-5674-4d01-a005-a400a7fd5f7f.png">

- Github PR review 후 dev branch로 PR merge 되면, Github Actions workflow 가 시작됩니다.
- Github actions에서 수정이 발생한 모듈을 확인하여 빌드 후 Docker Image화 하여 Docker hub로 올립니다.
- EC2에 ssh로 접속하여 Docker hub에 올라간 Docker Image파일을 받아와 실행시킵니다.
- 모든 과정이 완료되면 Discord를 통해 성공,실패 여부를 알립니다.

## 기술 스택

![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=java&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-%236DB33F.svg?style=for-the-badge&logo=springboot&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Security-%236DB33F.svg?style=for-the-badge&logo=springsecurity&logoColor=white)
![Websocket](https://img.shields.io/badge/Websocket-%23010101.svg?style=for-the-badge&logo=socket.io&logoColor=white)
![JUnit5](https://img.shields.io/badge/JUnit5-%2325A162.svg?style=for-the-badge&logo=JUnit5&logoColor=white)

![MySQL](https://img.shields.io/badge/mysql-%2300f.svg?style=for-the-badge&logo=mysql&logoColor=white)
![Redis](https://img.shields.io/badge/redis-%23DD0031.svg?style=for-the-badge&logo=redis&logoColor=white)
![Apache Kafka](https://img.shields.io/badge/Apache%20Kafka-000?style=for-the-badge&logo=apachekafka)
![GitHub Actions](https://img.shields.io/badge/github%20actions-%232671E5.svg?style=for-the-badge&logo=githubactions&logoColor=white)
![Docker](https://img.shields.io/badge/docker-%230db7ed.svg?style=for-the-badge&logo=docker&logoColor=white)

![AWS S3](https://img.shields.io/badge/Amazon%20S3-%23569A31.svg?style=for-the-badge&logo=amazons3&logoColor=white)
![AWS EC2](https://img.shields.io/badge/Amazon%20EC2-%23FF9900.svg?style=for-the-badge&logo=amazonec2&logoColor=white)
![AWS RDS](https://img.shields.io/badge/Amazon%20RDS-%23527FFF.svg?style=for-the-badge&logo=amazonrds&logoColor=white)


## 기술적 도전

- 설계 및 개발 고민 : https://jinsungone.notion.site/fb64bb18ff884273bd9dc857f79f492f?v=a23d6406ae614dfd90948f9d7341b9ae

### 1. Batch System 구현하기

> "부의마블" 프로젝트 상에는 존재하지 않는 휴양지 Owner의 행위를 대체하고 주식 거래 시스템과 같이 주기적으로 저장해야하는 거래 정보를 자동으로 생성하기 위하여 자동화 시스템을 도입하였습니다. 

- [종료된 공모를 마켓으로 전환하는 자동생성 작업 설계하기](https://jinsungone.notion.site/a1dbe96a254a4ab8909cf0baa80d4b5c)
- [스케줄링으로 전날 마켓 거래 정보 처리하기](https://jinsungone.notion.site/15d9cb823cb04a01a3f8033bc19ebc24)
- [최근 거래 휴양지는 어떻게 구축해야할까?](https://jinsungone.notion.site/71c960e150494f88a26870ed41826177)

### 2. QueryDSL 도입

> 복잡한 쿼리가 필요한 경우나 날짜 비교, Pagination을 적용할 때 QueryDSL로 최적화를 진행하였습니다.

- [Query 성능 최적화하기](https://jinsungone.notion.site/Query-00bd98178c3c478fa11844402ac84253)
- [Spring QueryDSL에서 날짜 비교 구현하기](https://jinsungone.notion.site/Spring-QueryDSL-d1bcb624da834966a76821ba15c4ba4e)

### 3. Redis 다양하게 활용해보기

> Redis 를 도입하며 랭킹 시스템과 동시성 제어를 구현하였습니다.

- [Redis 운용하기](https://jinsungone.notion.site/Redis-5f1df17a8a01486799385e499908be83)
- [랭킹 시스템은 어떻게 구현할까?](https://jinsungone.notion.site/32663fae2c34489ca14bc32c590ef96a)
- [매수, 매도 동시성 제어하기](https://jinsungone.notion.site/bc28d585f89240339808338e16c15280)

### 4. 기타

- [서버에서 CI/CD 구축하기](https://jinsungone.notion.site/CI-CD-8a726064221045b0ad70c203fbc659f1)
- [kafka 설정하기](https://jinsungone.notion.site/kafka-f2fc612bd15e4994bec114f0473fce37)

</br>

## Backend 주요 개발 사항
- Spring Security를 사용한 Google OAuth 로그인
- Redis Distribution Lock을 사용한 동시성 제어
- Redis Sorted Set을 활용한 랭킹 시스템 구현
- Kafaka 를 활용한 STOMP broker 연결
- Websocket을 사용한 실시간 마켓 거래 기능 구현
- Spring @Transactional 이용하여 주문 거래 로직 구현
- Batch 및 Schedular를 활용한 휴양지 상태 변경 자동화 및 일일 거래 정보 통계화
- Querydsl을 사용한 컴파일 시점에 SQL 오류 감지 및 동적 쿼리 작성.
- S3 이미지 업로드 및 관리
- POSTMAN을 통한 API Test 및 문서화
- Service Layer 및 Repository Layer 단위 테스트 작성
- Docker를 이용하여 CD 구현
