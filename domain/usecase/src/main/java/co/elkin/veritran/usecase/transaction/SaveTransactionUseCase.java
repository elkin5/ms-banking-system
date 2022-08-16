package co.elkin.veritran.usecase.transaction;

import co.elkin.veritran.model.transaction.Transaction;
import co.elkin.veritran.model.transaction.gateways.TransactionRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class SaveTransactionUseCase {
    private final TransactionRepository transactionRepository;

    public Mono<Transaction> save(Transaction transaction) {
        return transactionRepository.save(transaction);
    }
}
