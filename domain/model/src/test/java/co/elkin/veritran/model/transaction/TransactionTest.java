package co.elkin.veritran.model.transaction;

import co.elkin.veritran.model.Amount;
import co.elkin.veritran.model.TransactionType;
import co.elkin.veritran.model.account.Account;
import co.elkin.veritran.model.exceptions.OverdraftException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

class TransactionTest {
    @Test
    void whenAmountIsGreaterThanBalanceThenException() {
        Account account = Account.builder()
                .number(123L)
                .credit(new Amount(BigDecimal.valueOf(100)))
                .debit(new Amount(BigDecimal.valueOf(0)))
                .currency("USD")
                .build();

        Amount amount = new Amount(BigDecimal.valueOf(110));
        Assertions.assertThrows(OverdraftException.class, () -> new Transaction(
                TransactionType.WITHDRAW, account, amount, ""));
    }
}