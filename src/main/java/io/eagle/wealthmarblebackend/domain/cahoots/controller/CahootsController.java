package io.eagle.wealthmarblebackend.domain.cahoots.controller;

import io.eagle.wealthmarblebackend.domain.cahoots.dto.CreateCahootsDto;
import io.eagle.wealthmarblebackend.domain.cahoots.service.CahootsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/cahoots")
@RequiredArgsConstructor
public class CahootsController {
    private final CahootsService cahootsService;

    @PostMapping
    public ResponseEntity createCahoots (@Valid CreateCahootsDto createCahootsDto){
        System.out.println(createCahootsDto.toString());
        cahootsService.create(createCahootsDto);
        return ResponseEntity.ok().build();
    }
}
