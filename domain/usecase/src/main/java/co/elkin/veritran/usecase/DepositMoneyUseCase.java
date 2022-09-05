package co.elkin.veritran.usecase;

import co.elkin.veritran.model.Amount;
import co.elkin.veritran.model.TransactionType;
import co.elkin.veritran.model.account.Account;
import co.elkin.veritran.model.transaction.Transaction;
import co.elkin.veritran.usecase.account.SaveAccountUseCase;
import co.elkin.veritran.usecase.transaction.GenerateTransactionUseCase;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@RequiredArgsConstructor
public class DepositMoneyUseCase {
    private final GenerateTransactionUseCase generateTransactionUseCase;
    private final SaveAccountUseCase saveAccountUseCase;

    public Mono<Transaction> deposit(Long accountNumber, BigDecimal amount) {
        return generateTransactionUseCase.generateTransactionForAccount(accountNumber, new Amount(amount),
                        TransactionType.DEPOSIT)
                .flatMap(this::updateAccountBalance);
    }

    private Mono<Transaction> updateAccountBalance(Transaction transaction) {
        Account accountUpdate = transaction.getAccount().toBuilder()
                .credit(transaction.getAccount().getCredit().sum(transaction.getAmount()))
                .build();
        return saveAccountUseCase.saveAccount(accountUpdate)
                .map(account -> {
                    transaction.setAccount(account);
                    return transaction;
                });
    }
}
