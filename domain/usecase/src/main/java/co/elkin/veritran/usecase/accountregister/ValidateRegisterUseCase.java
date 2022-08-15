package co.elkin.veritran.usecase.accountregister;

import co.elkin.veritran.model.accountregister.AccountRegister;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class ValidateRegisterUseCase {
    public Mono<AccountRegister> validateById(Long id) {
        return null;
    }
}
