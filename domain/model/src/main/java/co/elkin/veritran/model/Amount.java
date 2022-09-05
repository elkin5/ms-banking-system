package co.elkin.veritran.model;

import co.elkin.veritran.model.exceptions.NegativeValueException;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class Amount {
    private BigDecimal value;

    public Amount(BigDecimal value) {
        this.value = value;
        isNegative();
    }

    private void isNegative() {
        if (this.value.compareTo(BigDecimal.ZERO) < 0) {
            throw new NegativeValueException("Negative value");
        }
    }

    public Amount sum(Amount amount) {
        amount.setValue(amount.getValue().add(this.value));
        return amount;
    }
}
