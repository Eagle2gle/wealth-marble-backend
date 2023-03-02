package io.eagle.domain.transaction.controller;

import io.eagle.auth.AuthDetails;
import io.eagle.common.ApiResponse;
import io.eagle.domain.transaction.dto.request.RecentTransactionRequestDto;
import io.eagle.domain.transaction.dto.request.TransactionRequestDto;
import io.eagle.domain.transaction.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

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
        @RequestParam(value = "page", defaultValue = "0") Integer page,
        @RequestParam(value = "startDate", required = false) String startDate,
        @RequestParam(value = "endDate", required = false) String endDate
    ) {
        if (checkTransactionsParams(startDate, endDate)) {
            HashMap<String, String> params = new HashMap<>();
            params.put("page", "0");
            params.put("startDate", "2023-03-01");
            params.put("endDate", "2023-03-02");
            return ApiResponse.createErrorWithContent(params, "해당 API에 필요한 param들에 대하여 다음과 같은 형식을 지켜주세요");
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return ApiResponse.createSuccess(
            transactionService.getTransactions(
                TransactionRequestDto.builder()
                    .vacationId(vacationId)
                    .page(page)
                    .startDate(LocalDate.parse(startDate, formatter))
                    .endDate(LocalDate.parse(endDate, formatter))
                    .build()
            )
        );
    }

    @GetMapping(value = "/transactions/subscribe-recent/{randomId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribeRecentTransaction(
        @PathVariable("randomId") String randomId
    ) {
        return transactionService.subscribeRecentTransaction(randomId);
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

    private Boolean checkTransactionsParams(String startDate, String endDate) {
        try{
            if (startDate == null || endDate == null || !matchesDatePattern(startDate) || !matchesDatePattern(endDate)) {
                return true;
            }
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            dateFormat.setLenient(false);

            dateFormat.parse(startDate);
            dateFormat.parse(endDate);
            return false;
        } catch (Exception e){
            e.printStackTrace();
            return true;
        }
    }

    private boolean matchesDatePattern(String dateString) {
        return dateString.matches("^\\d+\\-\\d+\\-\\d+");
    }

}
