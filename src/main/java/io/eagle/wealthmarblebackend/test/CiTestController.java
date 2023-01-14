package io.eagle.wealthmarblebackend.test;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class CiTestController {

    @GetMapping("/test")
    public String test(){
        System.out.println("${spring.datasource.username}");
        System.out.println("hi");
        return "spring ci test";
    }

}
