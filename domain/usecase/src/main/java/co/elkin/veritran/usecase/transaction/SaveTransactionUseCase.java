package co.elkin.veritran.usecase.transaction;

import co.elkin.veritran.model.transaction.Transaction;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class SaveTransactionUseCase {
    public Mono<Transaction> save(Transaction transaction) {
        return null;
    }
}
