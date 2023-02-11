package io.eagle.domain.transaction.controller;

import io.eagle.common.ApiResponse;
import io.eagle.domain.transaction.dto.TransactionRequestDto;
import io.eagle.domain.transaction.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @GetMapping("/auth/transactions/me")
    public ResponseEntity getTransactionHistoryByMine() {
        Long userId = 1L;
        return ResponseEntity.ok(transactionService.getMineTransaction(userId));
    }

    @GetMapping("/transactions/{vacationId}")
    public ApiResponse getTransactions(
        @PathVariable("vacationId") Long vacationId,
        @RequestParam("page") Integer page,
        @RequestParam("startDate") LocalDate startDate,
        @RequestParam("endDate") LocalDate endDate
    ) {
        return ApiResponse.createSuccess(
            transactionService.getTransactions(
                TransactionRequestDto.builder()
                    .vacationId(vacationId)
                    .page(page)
                    .startDate(startDate)
                    .endDate(endDate)
                    .build()
            )
        );
    }

}
