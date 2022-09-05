package co.elkin.veritran.model.transaction;

import co.elkin.veritran.model.Amount;
import co.elkin.veritran.model.TransactionType;
import co.elkin.veritran.model.account.Account;
import co.elkin.veritran.model.exceptions.OverdraftException;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Transaction {
    private TransactionType transactionType;
    private Account account;
    private Amount amount;
    private LocalDateTime creationDate;
    private String chanel;

    public Transaction(TransactionType transactionType, Account account, Amount amount, String chanel) {
        this.transactionType = transactionType;
        this.account = account;
        this.amount = amount;
        this.chanel = chanel;
        this.creationDate = LocalDateTime.now();
        this.amountIsOverdraft();
    }

    private void amountIsOverdraft() {
        if (this.transactionType.equals(TransactionType.WITHDRAW)
                && this.amount.getValue().compareTo(account.getBalance().getValue()) > 0) {
            throw new OverdraftException("Invalid amount: Balance must be greater than transaction amount");
        }
    }
}
