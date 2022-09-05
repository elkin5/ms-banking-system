package co.elkin.veritran.model;

import co.elkin.veritran.model.exceptions.NegativeValueException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

class AmountTest {

    @Test
    void whenAmountIsNegativeThenException() {
        BigDecimal value = BigDecimal.valueOf(-10);
        Assertions.assertThrows(NegativeValueException.class, () -> new Amount(value));
    }
}