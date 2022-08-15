package co.elkin.veritran.usecase.account;

import co.elkin.veritran.model.account.Account;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class FindAccountUseCase {
    public Mono<Account> findByAccountNumber(Long accountNumber) {
        return null;
    }
}
