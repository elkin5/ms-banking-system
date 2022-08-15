package co.elkin.veritran.usecase.account;

import co.elkin.veritran.model.account.Account;
import co.elkin.veritran.model.accountregister.AccountRegister;
import co.elkin.veritran.model.client.Client;
import co.elkin.veritran.model.transaction.Transaction;
import co.elkin.veritran.usecase.accountregister.ValidateRegisterUseCase;
import co.elkin.veritran.usecase.transaction.SaveTransactionUseCase;
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

import java.math.BigDecimal;
import java.time.LocalDate;

class DepositMoneyUseCaseTest {
    @Mock
    private FindAccountUseCase findAccountUseCase;
    @Mock
    private ValidateRegisterUseCase validateRegisterUseCase;
    @Mock
    private SaveTransactionUseCase saveTransactionUseCase;

    @InjectMocks
    private DepositMoneyUseCase depositMoneyUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName(" Given an existing client with id francisco with 100 USD in his account")
    void testDepositMoney() {
        Client client = Client.builder()
                .id(1L)
                .name("francisco")
                .email("francisco@example.com")
                .birthDate(LocalDate.of(2000, 1, 1))
                .lastName("Rodriguez")
                .build();
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
        Mockito.when(validateRegisterUseCase.validateById(2L))
                .thenReturn(Mono.just(accountRegister));
        Mockito.when(saveTransactionUseCase.save(transaction))
                .thenReturn(Mono.just(transaction));

        depositMoneyUseCase.deposit(1234567855555558L, BigDecimal.TEN)
                .as(StepVerifier::create)
                .expectNextMatches(accountUpdated -> {
                    Assertions.assertEquals(1234567855555558L, accountUpdated.getNumber());
                    Assertions.assertEquals(BigDecimal.valueOf(110), accountUpdated.getBalance());
                    return true;
                })
                .verifyComplete();
    }
}