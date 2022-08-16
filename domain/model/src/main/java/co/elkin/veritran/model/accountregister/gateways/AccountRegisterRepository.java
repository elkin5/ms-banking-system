package co.elkin.veritran.model.accountregister.gateways;

import co.elkin.veritran.model.accountregister.AccountRegister;
import reactor.core.publisher.Mono;

public interface AccountRegisterRepository {
    Mono<AccountRegister> findByAccountId(Long accountId);
}
