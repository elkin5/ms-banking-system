package co.elkin.veritran.usecase.transaction;

import co.elkin.veritran.model.Amount;
import co.elkin.veritran.model.TransactionType;
import co.elkin.veritran.model.transaction.Transaction;
import co.elkin.veritran.usecase.account.FindAccountUseCase;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class GenerateTransactionUseCase {
    private final FindAccountUseCase findAccountUseCase;
    private final SaveTransactionUseCase saveTransactionUseCase;

    public Mono<Transaction> generateTransactionForAccount(
            Long accountNumber, Amount amount, TransactionType type) {
        return findAccountUseCase.findByAccountNumber(accountNumber)
                .map(account -> new Transaction(type, account, amount, ""))
                .flatMap(saveTransactionUseCase::save);
    }
}
