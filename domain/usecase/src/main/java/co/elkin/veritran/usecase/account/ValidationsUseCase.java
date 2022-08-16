package co.elkin.veritran.usecase.account;

import co.elkin.veritran.model.account.Account;
import co.elkin.veritran.model.transaction.exceptions.TransactionException;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@RequiredArgsConstructor
public class ValidationsUseCase {
    public Mono<Account> validateOverdraft(Account account, BigDecimal amount) {
        if (amount.compareTo(account.getBalance()) > 0) {
            return Mono.error(new TransactionException("Invalid amount"));
        } else {
            return Mono.just(account);
        }
    }
}
