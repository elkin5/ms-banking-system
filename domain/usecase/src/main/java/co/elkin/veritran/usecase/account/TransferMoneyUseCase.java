package co.elkin.veritran.usecase.account;

import co.elkin.veritran.model.account.Account;
import co.elkin.veritran.model.transaction.Transaction;
import co.elkin.veritran.model.transfer.Transfer;
import co.elkin.veritran.model.transfer.gateways.TransferRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.math.BigDecimal;

@RequiredArgsConstructor
public class TransferMoneyUseCase {
    private final DepositMoneyUseCase depositMoneyUseCase;
    private final WithdrawMoneyUseCase withdrawMoneyUseCase;
    private final TransferRepository transferRepository;

    public Mono<Tuple2<Account, Account>> transfer(
            Long originAccountNumber, Long destinationAccountNumber, BigDecimal amount) {
        return withdrawMoneyUseCase.withdraw(originAccountNumber, amount)
                .zipWith(depositMoneyUseCase.deposit(destinationAccountNumber, amount))
                .flatMap(values -> {
                    Transaction transactionWithdraw = values.getT1().getT1();
                    Transaction transactionDeposit = values.getT2().getT1();

                    return transferRepository.save(Transfer.builder().originTransactionId(transactionWithdraw.getId())
                                    .destinationTransactionId(transactionDeposit.getId()).build())
                            .flatMap(unused -> Mono.zip(Mono.just(values.getT1().getT2()), Mono.just(values.getT2().getT2())));
                });
    }
}
