package io.eagle.domain.order.controller;

import io.eagle.common.ApiResponse;
import io.eagle.domain.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @GetMapping("/{vacationId}/list")
    public ApiResponse getTransactionAvailableList(@PathVariable("vacationId") Long vacationId){
        return ApiResponse.createSuccess(orderService.getOrderList(vacationId));
    }
}
