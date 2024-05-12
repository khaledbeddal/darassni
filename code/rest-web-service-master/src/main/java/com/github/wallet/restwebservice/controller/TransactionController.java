package com.github.wallet.restwebservice.controller;

import com.github.wallet.restwebservice.advice.ErrorResponse;
import com.github.wallet.restwebservice.advice.RateLimiterException;
import com.github.wallet.restwebservice.advice.WalletException;
import com.github.wallet.restwebservice.aspect.RateLimit;
import com.github.wallet.restwebservice.service.contracts.ITransactionService;
import com.github.wallet.restwebservice.service.contracts.IWalletService;
import com.github.wallet.restwebservice.service.models.TransactionDTO;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequestMapping("/api/v1")
public class TransactionController {

    Logger logger = LoggerFactory.getLogger(TransactionController.class);

    @Autowired
    private ITransactionService transactionService;
    @Autowired
    private IWalletService walletService;

    @GetMapping("/wallets/{id}/transactions")
    @RateLimit(limit = 50, duration = 60, unit = TimeUnit.SECONDS)
    @ApiOperation(value = "Find all transactions by given walletId", notes = "Returns a collection of transactions by given walletId")
    public ResponseEntity<?> getByWalletId(@PathVariable("id") long id) throws RateLimiterException {

        logger.info("TransactionController getByWalletId method calls for getting all transactions");

        List<TransactionDTO> transactions = transactionService.getTransactionsByWalletId(id);

        if(!transactions.isEmpty() && transactions.size() > 0){
            return ResponseEntity.ok().body(
                    transactions
                            .stream()
                            .collect(Collectors.toList())
            );
        }
        else{
            throw new WalletException(404, "Transactions not found");
        }
    }

    @PostMapping("/transaction")
    @RateLimit(limit = 50, duration = 60, unit = TimeUnit.SECONDS)
    @ApiOperation(value = "Add transaction of a given transaction and add balance", notes = "Create transaction and add balance")
    public ResponseEntity<?> creditOrDebit(@RequestBody TransactionDTO transaction) throws RateLimiterException{

        logger.info("TransactionController add method is calling");

        int typeId = transaction.getTypeId();
        double amount = transaction.getAmount();
        long walletId = transaction.getWalletId();
        long lectureId = transaction.getLectureId(); // Get the lectureId

        if(typeId != 1 && typeId != 2) return new ResponseEntity<>(new ErrorResponse("TypeId should be debit or credit", 400),HttpStatus.BAD_REQUEST);
        if(amount < 0) return new ResponseEntity<>(new ErrorResponse("Balance should not be negative", 400),HttpStatus.BAD_REQUEST);
        if(typeId == 1 && walletService.findById(transaction.getWalletId()).getBalance() < transaction.getAmount())
            return new ResponseEntity<>(new ErrorResponse("There is no enough balance", 400),HttpStatus.BAD_REQUEST);

        TransactionDTO createdTransaction;
        try{
            createdTransaction = transactionService.createTransactionAndChangeBalance(/*transaction.getGlobalId(),*/
                    typeId,
                    amount,
                    walletId,
                    lectureId); // Pass lectureId
            logger.info("TransactionController created transaction= " + createdTransaction.getWalletId());
        }
        catch (WalletException exc){
            logger.error("TransactionController creditOrDebit method has an error");
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/wallets/{userId}/{lectureId}/transactions")
    @RateLimit(limit = 50, duration = 60, unit = TimeUnit.SECONDS)
    @ApiOperation(value = "Find all debit transactions by given user and lectureId", notes = "Returns a collection of debit transactions by given user and lectureId")
    public ResponseEntity<?> getByUserIdAndLectureId(@PathVariable("userId") long userId, @PathVariable("lectureId") long lectureId) throws RateLimiterException {
        logger.info("TransactionController getByUserIdAndLectureId method calls for getting debit transactions");
        List<TransactionDTO> transactions = transactionService.getDebitTransactionsByUserIdAndLectureId(userId, lectureId);
        if (!transactions.isEmpty() && transactions.size() > 0) {
            return ResponseEntity.ok().body(
                    transactions
                            .stream()
                            .collect(Collectors.toList())
            );
        } else {
            throw new WalletException(404, "Debit transactions not found for the specified user and lecture");
        }
    }

    @GetMapping("/users/{userId}/transactions")
    @RateLimit(limit = 50, duration = 60, unit = TimeUnit.SECONDS)
    @ApiOperation(value = "Find all transactions by given user ID", notes = "Returns a collection of transactions by given user ID")
    public ResponseEntity<?> getByUserId(@PathVariable("userId") long userId) throws RateLimiterException {
        logger.info("TransactionController getByUserId method calls for getting transactions");
        List<TransactionDTO> transactions = transactionService.getTransactionsByUserId(userId);
        if (!transactions.isEmpty()) {
            return ResponseEntity.ok().body(transactions);
        } else {
            throw new WalletException(404, "Transactions not found for the specified user");
        }
    }

}
