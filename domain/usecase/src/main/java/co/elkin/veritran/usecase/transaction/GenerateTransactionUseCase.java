package co.elkin.veritran.usecase.transaction;

import co.elkin.veritran.model.account.Account;
import co.elkin.veritran.model.accountregister.AccountRegister;
import co.elkin.veritran.model.transaction.Transaction;
import co.elkin.veritran.model.transactiontype.TransactionType;
import co.elkin.veritran.model.transactiontype.enums.EnumTransactionType;
import co.elkin.veritran.usecase.account.FindAccountUseCase;
import co.elkin.veritran.usecase.account.ValidationsUseCase;
import co.elkin.veritran.usecase.accountregister.FindAccountRegisterUseCase;
import co.elkin.veritran.usecase.transactiontype.FindTransactionTypeUseCase;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.math.BigDecimal;

@RequiredArgsConstructor
public class GenerateTransactionUseCase {
    private final FindAccountUseCase findAccountUseCase;
    private final FindAccountRegisterUseCase findAccountRegisterUseCase;
    private final FindTransactionTypeUseCase findTransactionTypeUseCase;
    private final SaveTransactionUseCase saveTransactionUseCase;
    private final ValidationsUseCase validationsUseCase;

    public Mono<Tuple2<Transaction, Account>> generateTransactionForAccount(
            Long accountNumber, BigDecimal amount, EnumTransactionType type) {
        return findAccountUseCase.findByAccountNumber(accountNumber)
                .flatMap(account -> validationsUseCase.validateOverdraft(account, amount))
                .flatMap(account -> Mono.zip(Mono.just(account),
                        findAccountRegisterUseCase.findByAccountId(account.getId())))
                .flatMap(values -> {
                    Account account = values.getT1();
                    AccountRegister accountRegister = values.getT2();
                    return findTransactionTypeUseCase.findByName(type)
                            .map(TransactionType::getId)
                            .map(transactionTypeId -> Transaction.builder()
                                    .transactionTypeId(transactionTypeId)
                                    .accountId(account.getId())
                                    .clientId(accountRegister.getClientId())
                                    .amount(amount)
                                    .build())
                            .flatMap(transaction -> Mono.zip(
                                    saveTransactionUseCase.save(transaction), Mono.just(account)));
                });
    }
}
