package io.eagle.domain.transaction.controller;

import io.eagle.domain.transaction.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @GetMapping("/transactions/me")
    public ResponseEntity getTransactionHistoryByMine() {
        Long userId = 1L;
        return ResponseEntity.ok(transactionService.getMineTransaction(userId));
    }

}
