package co.elkin.veritran.usecase.account;

import co.elkin.veritran.model.account.Account;
import co.elkin.veritran.model.transaction.Transaction;
import co.elkin.veritran.model.transaction.exceptions.TransactionException;
import co.elkin.veritran.model.transactiontype.enums.EnumTransactionType;
import co.elkin.veritran.usecase.transaction.GenerateTransactionUseCase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.math.BigDecimal;

class DepositMoneyUseCaseTest {
    @Mock
    private GenerateTransactionUseCase generateTransactionUseCase;
    @Mock
    private SaveAccountUseCase saveAccountUseCase;

    @InjectMocks
    private DepositMoneyUseCase depositMoneyUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("When he deposits 10 USD into his account Then the balance of his account is 110 USD")
    void testDepositMoney() {
        Account account = Account.builder()
                .id(2L)
                .number(1234567855555558L)
                .balance(BigDecimal.valueOf(100))
                .credit(BigDecimal.valueOf(100))
                .debit(BigDecimal.ZERO)
                .currency("USD")
                .build();
        Account accountUpdated = Account.builder()
                .id(2L)
                .number(1234567855555558L)
                .balance(BigDecimal.valueOf(110))
                .credit(BigDecimal.valueOf(110))
                .debit(BigDecimal.ZERO)
                .currency("USD")
                .build();
        Transaction transaction = Transaction.builder()
                .id(3L)
                .accountId(2L)
                .clientId(1L)
                .transactionTypeId(1L)
                .amount(BigDecimal.TEN)
                .build();

        Tuple2<Transaction, Account> values = Tuples.of(transaction, account);
        Mockito.when(generateTransactionUseCase.generateTransactionForAccount(1234567855555558L,
                        BigDecimal.TEN, EnumTransactionType.DEPOSIT))
                .thenReturn(Mono.just(values));

        Mockito.when(saveAccountUseCase.saveAccount(accountUpdated))
                .thenReturn(Mono.just(accountUpdated));

        depositMoneyUseCase.deposit(1234567855555558L, BigDecimal.TEN)
                .as(StepVerifier::create)
                .expectNextMatches(result -> {
                    Assertions.assertEquals(1234567855555558L, result.getNumber());
                    Assertions.assertEquals(BigDecimal.valueOf(110), result.getBalance());
                    return true;
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("when the amount is negative then Exception")
    void testDepositMoney2() {
        depositMoneyUseCase.deposit(1234567855555558L, BigDecimal.TEN)
                .as(StepVerifier::create)
                .verifyError(TransactionException.class);
    }
}