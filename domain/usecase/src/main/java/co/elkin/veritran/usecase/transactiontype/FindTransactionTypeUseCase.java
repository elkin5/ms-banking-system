package co.elkin.veritran.usecase.transactiontype;

import co.elkin.veritran.model.transactiontype.TransactionType;
import co.elkin.veritran.model.transactiontype.gateways.TransactionTypeRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class FindTransactionTypeUseCase {
    private final TransactionTypeRepository transactionTypeRepository;

    public Mono<TransactionType> findByName(String name) {
        return transactionTypeRepository.findByName(name);
    }
}
