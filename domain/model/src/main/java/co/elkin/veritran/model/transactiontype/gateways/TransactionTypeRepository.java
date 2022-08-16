package co.elkin.veritran.model.transactiontype.gateways;

import co.elkin.veritran.model.transactiontype.TransactionType;
import reactor.core.publisher.Mono;

public interface TransactionTypeRepository {
    Mono<TransactionType> findByName(String name);
}
