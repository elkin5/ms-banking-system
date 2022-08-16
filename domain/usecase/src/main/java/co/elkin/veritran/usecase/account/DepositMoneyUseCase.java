package co.elkin.veritran.usecase.account;

import co.elkin.veritran.model.account.Account;
import co.elkin.veritran.model.accountregister.AccountRegister;
import co.elkin.veritran.model.transaction.Transaction;
import co.elkin.veritran.model.transactiontype.TransactionType;
import co.elkin.veritran.usecase.accountregister.ValidateRegisterUseCase;
import co.elkin.veritran.usecase.transaction.SaveTransactionUseCase;
import co.elkin.veritran.usecase.transactiontype.FindTransactionTypeUseCase;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuple3;

import java.math.BigDecimal;

@RequiredArgsConstructor
public class DepositMoneyUseCase {
    private final FindAccountUseCase findAccountUseCase;
    private final ValidateRegisterUseCase validateRegisterUseCase;
    private final FindTransactionTypeUseCase findTransactionTypeUseCase;
    private final SaveTransactionUseCase saveTransactionUseCase;
    private final SaveAccountUseCase saveAccountUseCase;

    public Mono<Account> deposit(Long accountNumber, BigDecimal amount) {
        return findAccountUseCase.findByAccountNumber(accountNumber)
                .flatMap(account -> Mono.zip(Mono.just(account),
                        validateRegisterUseCase.validateById(account.getId()), Mono.just(amount)))
                .flatMap(this::generateTransaction)
                .flatMap(this::updateAccountBalance);
    }

    private Mono<Tuple2<Transaction, Account>> generateTransaction(
            Tuple3<Account, AccountRegister, BigDecimal> values) {
        Account account = values.getT1();
        AccountRegister accountRegister = values.getT2();
        BigDecimal amount = values.getT3();
        return findTransactionTypeUseCase.findByName("withdrawal")
                .map(TransactionType::getId)
                .map(transactionTypeId -> Transaction.builder()
                        .transactionTypeId(transactionTypeId)
                        .accountId(account.getId())
                        .clientId(accountRegister.getClientId())
                        .amount(amount)
                        .build())
                .flatMap(transaction -> Mono.zip(saveTransactionUseCase.save(transaction), Mono.just(account)));
    }

    private Mono<Account> updateAccountBalance(Tuple2<Transaction, Account> values) {
        Transaction transaction = values.getT1();
        Account account = values.getT2();
        BigDecimal credit = account.getCredit().add(transaction.getAmount());
        BigDecimal balance = credit.subtract(account.getDebit());
        Account accountUpdated = account.toBuilder()
                .credit(credit)
                .balance(balance)
                .build();
        return saveAccountUseCase.saveAccount(accountUpdated);
    }
}
