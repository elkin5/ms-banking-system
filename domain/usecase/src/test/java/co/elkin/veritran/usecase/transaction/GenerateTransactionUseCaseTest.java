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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import reactor.util.function.Tuples;

import java.math.BigDecimal;

class GenerateTransactionUseCaseTest {
    @Mock
    private FindAccountUseCase findAccountUseCase;
    @Mock
    private FindAccountRegisterUseCase findAccountRegisterUseCase;
    @Mock
    private FindTransactionTypeUseCase findTransactionTypeUseCase;
    @Mock
    private SaveTransactionUseCase saveTransactionUseCase;
    @Mock
    private ValidationsUseCase validationsUseCase;

    @InjectMocks
    private GenerateTransactionUseCase generateTransactionUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void generateTransactionForAccount() {
        Account account = Account.builder()
                .id(2L)
                .number(1234567855555558L)
                .balance(BigDecimal.valueOf(100))
                .credit(BigDecimal.valueOf(100))
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

        AccountRegister accountRegister = AccountRegister.builder()
                .id(1L)
                .accountId(2L)
                .clientId(1L)
                .status("ACTIVE")
                .build();

        Mockito.when(findAccountUseCase.findByAccountNumber(1234567855555558L))
                .thenReturn(Mono.just(account));
        Mockito.when(validationsUseCase.validateOverdraft(account, BigDecimal.valueOf(10), EnumTransactionType.DEPOSIT))
                .thenReturn(Mono.just(account));
        Mockito.when(validationsUseCase.validateNegativeAmount(account, BigDecimal.valueOf(10)))
                .thenReturn(Mono.just(account));
        Mockito.when(findAccountRegisterUseCase.findByAccountId(2L))
                .thenReturn(Mono.just(accountRegister));
        Mockito.when(findTransactionTypeUseCase.findByName(EnumTransactionType.DEPOSIT))
                .thenReturn(Mono.just(TransactionType.builder().id(1L).name("deposit").build()));
        Mockito.when(saveTransactionUseCase.save(transaction.toBuilder().id(null).build()))
                .thenReturn(Mono.just(transaction));

        generateTransactionUseCase.generateTransactionForAccount(1234567855555558L, BigDecimal.valueOf(10),
                        EnumTransactionType.DEPOSIT)
                .as(StepVerifier::create)
                .expectNext(Tuples.of(transaction, account))
                .verifyComplete();
    }
}