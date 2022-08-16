package co.elkin.veritran.model.transactiontype.gateways;

import co.elkin.veritran.model.transactiontype.TransactionType;
import co.elkin.veritran.model.transactiontype.enums.EnumTransactionType;
import reactor.core.publisher.Mono;

public interface TransactionTypeRepository {
    Mono<TransactionType> findByName(EnumTransactionType name);
}
