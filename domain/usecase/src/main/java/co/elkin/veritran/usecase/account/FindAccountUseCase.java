package co.elkin.veritran.usecase.account;

import co.elkin.veritran.model.account.Account;
import co.elkin.veritran.model.account.gateways.AccountRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class FindAccountUseCase {
    private final AccountRepository accountRepository;

    public Mono<Account> findByAccountNumber(Long accountNumber) {
        return accountRepository.findByNumber(accountNumber);
    }
}
