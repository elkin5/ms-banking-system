package co.elkin.veritran.usecase;

import co.elkin.veritran.model.Amount;
import co.elkin.veritran.model.TransactionType;
import co.elkin.veritran.model.account.Account;
import co.elkin.veritran.model.transaction.Transaction;
import co.elkin.veritran.model.transfer.Transfer;
import co.elkin.veritran.model.transfer.gateways.TransferRepository;
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

class TransferMoneyUseCaseTest {

    @Mock
    private DepositMoneyUseCase depositMoneyUseCase;
    @Mock
    private WithdrawMoneyUseCase withdrawMoneyUseCase;
    @Mock
    private TransferRepository transferRepository;

    @InjectMocks
    private TransferMoneyUseCase transferMoneyUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testTransferMoney() {
        Long originAccountNumber = 1234567855555558L;

        Account originAccount = Account.builder()
                .number(originAccountNumber)
                .credit(new Amount(BigDecimal.valueOf(100)))
                .debit(new Amount(BigDecimal.valueOf(10)))
                .currency("USD")
                .build();

        Long destinationAccountNumber = 9876567855555600L;

        Account destinationAccount = Account.builder()
                .number(destinationAccountNumber)
                .credit(new Amount(BigDecimal.valueOf(70)))
                .debit(new Amount(BigDecimal.valueOf(10)))
                .currency("USD")
                .build();

        Amount amount = new Amount(BigDecimal.TEN);

        Transaction transactionWithdraw = new Transaction(TransactionType.WITHDRAW, originAccount, amount, "");

        Transaction transactionDeposit = new Transaction(TransactionType.DEPOSIT, destinationAccount, amount, "");

        Transfer transfer = Transfer.builder()
                .withdraw(transactionWithdraw)
                .deposit(transactionDeposit)
                .build();

        Mockito.when(withdrawMoneyUseCase.withdraw(originAccountNumber, BigDecimal.TEN))
                .thenReturn(Mono.just(transactionWithdraw));
        Mockito.when(depositMoneyUseCase.deposit(destinationAccountNumber, BigDecimal.TEN))
                .thenReturn(Mono.just(transactionDeposit));
        Mockito.when(transferRepository.save(transfer))
                .thenReturn(Mono.just(transfer));

        transferMoneyUseCase.transfer(originAccountNumber, destinationAccountNumber, BigDecimal.TEN)
                .as(StepVerifier::create)
                .expectNextMatches(result -> {
                    Account origin = result.getWithdraw().getAccount();
                    Account destination = result.getDeposit().getAccount();
                    Assertions.assertEquals(BigDecimal.valueOf(90), origin.getBalance().getValue());
                    Assertions.assertEquals(BigDecimal.valueOf(10), origin.getDebit().getValue());
                    Assertions.assertEquals(BigDecimal.valueOf(60), destination.getBalance().getValue());
                    Assertions.assertEquals(BigDecimal.valueOf(70), destination.getCredit().getValue());

                    return true;
                }).verifyComplete();
    }
}