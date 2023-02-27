package io.eagle.domain.transaction.controller;

import io.eagle.auth.AuthDetails;
import io.eagle.common.ApiResponse;
import io.eagle.domain.transaction.dto.request.TransactionRequestDto;
import io.eagle.domain.transaction.dto.response.RecentTransactionDto;
import io.eagle.domain.transaction.service.TransactionService;
import io.eagle.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @GetMapping("/auth/transactions/me")
    public ApiResponse getTransactionHistoryByMine(@AuthenticationPrincipal AuthDetails authDetails) {
        return ApiResponse.createSuccess(transactionService.getMineTransaction(authDetails.getUser()));
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

    @GetMapping("/transactions/subscribe-recent/{randomId}")
    public ApiResponse subscribeRecentTransaction(
        @PathVariable("randomId") String randomId
    ) {
        return ApiResponse.createSuccess(transactionService.subscribeRecentTransaction(randomId));
    }

    @PostMapping("/transactions/publish-recent")
    public ApiResponse publishRecentTransaction(@RequestBody RecentTransactionDto recentTransactionDto) {
        try {
            transactionService.publishRecentTransaction(recentTransactionDto);
            return ApiResponse.createSuccessWithNoContent();
        } catch (Exception e) {
            return ApiResponse.createError("Cannot Send Data");
        }
    }

}
