package co.elkin.veritran.usecase;

import co.elkin.veritran.model.Amount;
import co.elkin.veritran.model.TransactionType;
import co.elkin.veritran.model.account.Account;
import co.elkin.veritran.model.client.Client;
import co.elkin.veritran.model.transaction.Transaction;
import co.elkin.veritran.usecase.account.SaveAccountUseCase;
import co.elkin.veritran.usecase.transaction.GenerateTransactionUseCase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDate;

class WithdrawMoneyUseCaseTest {
    @Mock
    private GenerateTransactionUseCase generateTransactionUseCase;
    @Mock
    private SaveAccountUseCase saveAccountUseCase;

    @InjectMocks
    private WithdrawMoneyUseCase withdrawMoneyUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testWithdraw() {
        Long accountNumber = 1234567855555558L;
        Client client = Client.builder()
                .documentNumber("1234")
                .name("John")
                .lastName("Snow")
                .birthDate(LocalDate.now())
                .build();

        Account account = Account.builder()
                .number(accountNumber)
                .credit(new Amount(BigDecimal.valueOf(100)))
                .debit(new Amount(BigDecimal.valueOf(0)))
                .currency("USD")
                .client(client)
                .build();

        Account accountUpdated = Account.builder()
                .number(accountNumber)
                .credit(new Amount(BigDecimal.valueOf(100)))
                .debit(new Amount(BigDecimal.TEN))
                .currency("USD")
                .client(client)
                .build();

        Amount amount = new Amount(BigDecimal.TEN);

        Transaction transaction = new Transaction(TransactionType.WITHDRAW, account, amount, "");

        Mockito.when(generateTransactionUseCase.generateTransactionForAccount(accountNumber,
                        amount, TransactionType.WITHDRAW))
                .thenReturn(Mono.just(transaction));

        Mockito.when(saveAccountUseCase.saveAccount(accountUpdated))
                .thenReturn(Mono.just(accountUpdated));

        withdrawMoneyUseCase.withdraw(accountNumber, BigDecimal.TEN)
                .as(StepVerifier::create)
                .expectNextMatches(result -> {
                    Assertions.assertEquals(accountNumber, result.getAccount().getNumber());
                    Assertions.assertEquals(BigDecimal.valueOf(90), result.getAccount().getBalance().getValue());
                    return true;
                })
                .verifyComplete();
    }
}