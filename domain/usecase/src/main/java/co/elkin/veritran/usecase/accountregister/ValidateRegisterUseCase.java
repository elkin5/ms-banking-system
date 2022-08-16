package co.elkin.veritran.usecase.accountregister;

import co.elkin.veritran.model.accountregister.AccountRegister;
import co.elkin.veritran.model.accountregister.gateways.AccountRegisterRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class ValidateRegisterUseCase {
    private final AccountRegisterRepository accountRegisterRepository;

    public Mono<AccountRegister> validateById(Long accountId) {
        return accountRegisterRepository.findByAccountId(accountId);
    }
}
