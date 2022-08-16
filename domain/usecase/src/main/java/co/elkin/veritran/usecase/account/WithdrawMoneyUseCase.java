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
public class WithdrawMoneyUseCase {
    private final GenerateTransactionUseCase generateTransactionUseCase;
    private final SaveAccountUseCase saveAccountUseCase;

    public Mono<Account> withdraw(Long accountNumber, BigDecimal amount) {
        return generateTransactionUseCase.generateTransactionForAccount(accountNumber, amount,
                        EnumTransactionType.WITHDRAWAL)
                .flatMap(this::updateAccountBalance);
    }

    private Mono<Account> updateAccountBalance(Tuple2<Transaction, Account> values) {
        Transaction transaction = values.getT1();
        Account account = values.getT2();
        BigDecimal debit = account.getDebit().add(transaction.getAmount());
        BigDecimal balance = account.getCredit().subtract(debit);
        Account accountUpdated = account.toBuilder()
                .debit(debit)
                .balance(balance)
                .build();
        return saveAccountUseCase.saveAccount(accountUpdated);
    }
}
