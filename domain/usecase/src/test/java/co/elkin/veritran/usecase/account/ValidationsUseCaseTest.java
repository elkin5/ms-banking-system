package co.elkin.veritran.usecase.account;

import co.elkin.veritran.model.account.Account;
import co.elkin.veritran.model.transaction.exceptions.TransactionException;
import co.elkin.veritran.model.transactiontype.enums.EnumTransactionType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

class ValidationsUseCaseTest {
    @InjectMocks
    private ValidationsUseCase validationsUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("When the amount is greater than the balance then Exception")
    void validateOverdraft() {
        Account account = Account.builder()
                .id(2L)
                .number(1234567855555558L)
                .balance(BigDecimal.valueOf(90))
                .credit(BigDecimal.valueOf(100))
                .debit(BigDecimal.TEN)
                .currency("USD")
                .build();
        BigDecimal amount = BigDecimal.valueOf(100);

        validationsUseCase.validateOverdraft(account, amount, EnumTransactionType.WITHDRAWAL)
                .as(StepVerifier::create)
                .expectErrorMatches(throwable -> {
                    Assertions.assertEquals(TransactionException.class, throwable.getClass());
                    Assertions.assertTrue(throwable.getMessage().contains("Invalid amount"));
                    return true;
                })
                .verify();
    }

    @Test
    @DisplayName("when the amount is negative then Exception")
    void validateNegativeAmount() {
        Account account = Account.builder()
                .id(2L)
                .number(1234567855555558L)
                .balance(BigDecimal.valueOf(90))
                .credit(BigDecimal.valueOf(100))
                .debit(BigDecimal.TEN)
                .currency("USD")
                .build();
        BigDecimal amount = BigDecimal.valueOf(-10);

        validationsUseCase.validateNegativeAmount(account, amount)
                .as(StepVerifier::create)
                .expectErrorMatches(throwable -> {
                    Assertions.assertEquals(TransactionException.class, throwable.getClass());
                    Assertions.assertTrue(throwable.getMessage().contains("Negative or Zero amount"));
                    return true;
                })
                .verify();
    }
}