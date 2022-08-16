package co.elkin.veritran.model.account.gateways;

import co.elkin.veritran.model.account.Account;
import reactor.core.publisher.Mono;

public interface AccountRepository {
    Mono<Account> findByNumber(Long accountNumber);
    Mono<Account> save(Account account);
}
