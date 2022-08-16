package co.elkin.veritran.usecase.account;

import co.elkin.veritran.model.account.Account;
import co.elkin.veritran.model.transaction.exceptions.TransactionException;
import co.elkin.veritran.model.transactiontype.enums.EnumTransactionType;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@RequiredArgsConstructor
public class ValidationsUseCase {
    public Mono<Account> validateOverdraft(Account account, BigDecimal amount, EnumTransactionType type) {
        if (type.equals(EnumTransactionType.WITHDRAWAL) && amount.compareTo(account.getBalance()) > 0) {
            return Mono.error(new TransactionException("Invalid amount"));
        } else {
            return Mono.just(account);
        }
    }

    public Mono<Account> validateNegativeAmount(Account account, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            return Mono.error(new TransactionException("Negative amount"));
        } else {
            return Mono.just(account);
        }
    }
}
