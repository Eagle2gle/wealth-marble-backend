package io.eagle.domain.transaction.controller;

import io.eagle.auth.AuthDetails;
import io.eagle.common.ApiResponse;
import io.eagle.domain.transaction.dto.request.RecentTransactionRequestDto;
import io.eagle.domain.transaction.dto.request.TransactionRequestDto;
import io.eagle.domain.transaction.service.TransactionService;
import lombok.RequiredArgsConstructor;
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
    public ApiResponse publishRecentTransaction(@RequestBody RecentTransactionRequestDto request) {
        try {
            transactionService.publishRecentTransaction(request);
            return ApiResponse.createSuccessWithNoContent();
        } catch (Exception e) {
            return ApiResponse.createError("Cannot Send Data");
        }
    }

}
