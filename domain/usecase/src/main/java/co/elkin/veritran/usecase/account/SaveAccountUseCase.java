package co.elkin.veritran.usecase.account;

import co.elkin.veritran.model.account.Account;
import co.elkin.veritran.model.account.gateways.AccountRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class SaveAccountUseCase {
    private final AccountRepository accountRepository;

    public Mono<Account> saveAccount(Account account) {
        return accountRepository.save(account);
    }
}
