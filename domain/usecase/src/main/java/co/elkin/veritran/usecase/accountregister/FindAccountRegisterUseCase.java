package co.elkin.veritran.usecase.accountregister;

import co.elkin.veritran.model.accountregister.AccountRegister;
import co.elkin.veritran.model.accountregister.gateways.AccountRegisterRepository;
import co.elkin.veritran.model.transaction.exceptions.TransactionException;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class FindAccountRegisterUseCase {
    private final AccountRegisterRepository accountRegisterRepository;

    public Mono<AccountRegister> findByAccountId(Long accountId) {
        return accountRegisterRepository.findByAccountId(accountId)
                .flatMap(accountRegister -> {
                    if (accountRegister.getStatus().equals("INACTIVE")) {
                        return Mono.error(new TransactionException("Account is inactive"));
                    }
                    return Mono.just(accountRegister);
                })
                .switchIfEmpty(Mono.error(new TransactionException("Account not registered")));
    }
}
