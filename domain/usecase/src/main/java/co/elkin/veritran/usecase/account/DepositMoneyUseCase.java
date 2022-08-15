package co.elkin.veritran.usecase.account;

import co.elkin.veritran.model.account.Account;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@RequiredArgsConstructor
public class DepositMoneyUseCase {
    private final FindAccountUseCase findAccountUseCase;

    public Mono<Account> deposit(Long accountNumber, BigDecimal amount) {
        return null;
    }
}
