package co.elkin.veritran.usecase.account;

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
import reactor.util.function.Tuples;

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
        Account originAccount = Account.builder()
                .id(1L)
                .number(1234567855555558L)
                .balance(BigDecimal.valueOf(90))
                .credit(BigDecimal.valueOf(100))
                .debit(BigDecimal.TEN)
                .currency("USD")
                .build();

        Account destinationAccount = Account.builder()
                .id(2L)
                .number(1234567855555600L)
                .balance(BigDecimal.valueOf(60))
                .credit(BigDecimal.valueOf(70))
                .debit(BigDecimal.TEN)
                .currency("USD")
                .build();

        Transaction transactionWithdraw = Transaction.builder()
                .id(4L)
                .accountId(1L)
                .clientId(10L)
                .transactionTypeId(1L)
                .amount(BigDecimal.TEN)
                .build();

        Transaction transactionDeposit = Transaction.builder()
                .id(3L)
                .accountId(2L)
                .clientId(1L)
                .transactionTypeId(2L)
                .amount(BigDecimal.TEN)
                .build();

        Transfer transfer = Transfer.builder()
                .originTransactionId(4L)
                .destinationTransactionId(3L)
                .build();

        Mockito.when(withdrawMoneyUseCase.withdraw(1234567855555558L, BigDecimal.TEN))
                .thenReturn(Mono.just(Tuples.of(transactionWithdraw, originAccount)));
        Mockito.when(depositMoneyUseCase.deposit(1234567855555600L, BigDecimal.TEN))
                .thenReturn(Mono.just(Tuples.of(transactionDeposit, destinationAccount)));
        Mockito.when(transferRepository.save(transfer))
                .thenReturn(Mono.just(transfer));

        transferMoneyUseCase.transfer(1234567855555558L, 1234567855555600L, BigDecimal.TEN)
                .as(StepVerifier::create)
                .expectNextMatches(result -> {
                    Account origin = result.getT1();
                    Account destination = result.getT2();
                    Assertions.assertEquals(BigDecimal.valueOf(90), origin.getBalance());
                    Assertions.assertEquals(BigDecimal.valueOf(10), origin.getDebit());
                    Assertions.assertEquals(BigDecimal.valueOf(60), destination.getBalance());
                    Assertions.assertEquals(BigDecimal.valueOf(70), destination.getCredit());

                    return true;
                }).verifyComplete();
    }
}