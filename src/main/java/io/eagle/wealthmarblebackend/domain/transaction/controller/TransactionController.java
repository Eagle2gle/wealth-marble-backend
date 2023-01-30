package io.eagle.wealthmarblebackend.domain.transaction.controller;

import io.eagle.wealthmarblebackend.domain.transaction.dto.UserTransactionInfoDto;
import io.eagle.wealthmarblebackend.domain.transaction.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @GetMapping("/me")
    public ResponseEntity getTransactionHistoryByMine() {
        Long userId = 1L;
        return ResponseEntity.ok(transactionService.getMineTransaction(userId));
    }

}
