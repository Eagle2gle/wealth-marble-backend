package io.eagle.wealthmarblebackend.test;

import io.eagle.wealthmarblebackend.exception.ApiException;
import io.eagle.wealthmarblebackend.exception.error.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class CiTestController {

    @GetMapping("/test")
    public String test(){
        System.out.println("${spring.datasource.username}");
        return "spring ci test";
    }

    @GetMapping("/exception/{id}")
    public String testParamError(@PathVariable("id") Long id ){
        return "param id : "+ id;
    }

    @GetMapping("/exception/api")
    public String testApiError( ){
        throw new ApiException(ErrorCode.VACATION_NOT_FOUND);
//        return "pass";
    }

}
