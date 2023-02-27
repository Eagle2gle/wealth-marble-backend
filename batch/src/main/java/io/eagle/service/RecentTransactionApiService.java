package io.eagle.service;

import io.eagle.domain.RecentTransactionRequestVO;
import io.eagle.entity.Transaction;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@Service
public class RecentTransactionApiService {

    public Boolean service(RecentTransactionRequestVO vo) {
        if (vo == null) {
            return false;
        }

        // API 연동 작업
        RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder();
        RestTemplate restTemplate = restTemplateBuilder.errorHandler(new ResponseErrorHandler() {
            @Override
            public boolean hasError(ClientHttpResponse response) throws IOException {
                return false;
            }

            @Override
            public void handleError(ClientHttpResponse response) throws IOException {

            }
        }).build();

        // 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<RecentTransactionRequestVO> httpEntity = new HttpEntity<>(vo, headers);
        return doApiService(restTemplate, httpEntity);
    }

    public Boolean doApiService(RestTemplate restTemplate, HttpEntity vo) {
        ResponseEntity<String> response = restTemplate.postForEntity("http://localhost:8080/api/transactions/publish-recent", vo, String.class);
        System.out.println(response);
        return response.getStatusCodeValue() < 400;
    }

}
