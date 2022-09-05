package co.elkin.veritran.usecase.transaction;

import co.elkin.veritran.model.Amount;
import co.elkin.veritran.model.TransactionType;
import co.elkin.veritran.model.account.Account;
import co.elkin.veritran.model.transaction.Transaction;
import co.elkin.veritran.usecase.account.FindAccountUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

class GenerateTransactionUseCaseTest {
    @Mock
    private FindAccountUseCase findAccountUseCase;
    @Mock
    private SaveTransactionUseCase saveTransactionUseCase;

    @InjectMocks
    private GenerateTransactionUseCase generateTransactionUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void generateTransactionForAccount() {
        Long accountNumber = 1234567855555558L;

        Account account = Account.builder()
                .number(accountNumber)
                .credit(new Amount(BigDecimal.valueOf(100)))
                .debit(new Amount(BigDecimal.valueOf(0)))
                .currency("USD")
                .build();

        Amount amount = new Amount(BigDecimal.TEN);

        Transaction transaction = new Transaction(TransactionType.DEPOSIT, account, amount, "");

        Mockito.when(findAccountUseCase.findByAccountNumber(accountNumber))
                .thenReturn(Mono.just(account));
        Mockito.when(saveTransactionUseCase.save(Mockito.any()))
                .thenReturn(Mono.just(transaction));

        generateTransactionUseCase.generateTransactionForAccount(accountNumber, amount, TransactionType.DEPOSIT)
                .as(StepVerifier::create)
                .expectNext(transaction)
                .verifyComplete();
    }
}