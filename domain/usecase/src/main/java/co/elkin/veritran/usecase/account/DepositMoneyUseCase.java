package co.elkin.veritran.usecase.account;

import co.elkin.veritran.model.account.Account;
import co.elkin.veritran.model.transaction.Transaction;
import co.elkin.veritran.model.transactiontype.enums.EnumTransactionType;
import co.elkin.veritran.usecase.transaction.GenerateTransactionUseCase;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.math.BigDecimal;

@RequiredArgsConstructor
public class DepositMoneyUseCase {
    private final GenerateTransactionUseCase generateTransactionUseCase;
    private final SaveAccountUseCase saveAccountUseCase;

    public Mono<Account> deposit(Long accountNumber, BigDecimal amount) {
        return generateTransactionUseCase.generateTransactionForAccount(accountNumber, amount,
                        EnumTransactionType.DEPOSIT)
                .flatMap(this::updateAccountBalance);
    }

    private Mono<Account> updateAccountBalance(Tuple2<Transaction, Account> values) {
        Transaction transaction = values.getT1();
        Account account = values.getT2();
        BigDecimal credit = account.getCredit().add(transaction.getAmount());
        BigDecimal balance = credit.subtract(account.getDebit());
        Account accountUpdated = account.toBuilder()
                .credit(credit)
                .balance(balance)
                .build();
        return saveAccountUseCase.saveAccount(accountUpdated);
    }
}
