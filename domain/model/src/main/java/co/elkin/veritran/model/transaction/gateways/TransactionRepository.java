package co.elkin.veritran.model.transaction.gateways;

import co.elkin.veritran.model.transaction.Transaction;
import reactor.core.publisher.Mono;

public interface TransactionRepository {
    Mono<Transaction> save(Transaction transaction);
}
