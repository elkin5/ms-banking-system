package co.elkin.veritran.usecase;

import co.elkin.veritran.model.transaction.Transaction;
import co.elkin.veritran.model.transfer.Transfer;
import co.elkin.veritran.model.transfer.gateways.TransferRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@RequiredArgsConstructor
public class TransferMoneyUseCase {
    private final DepositMoneyUseCase depositMoneyUseCase;
    private final WithdrawMoneyUseCase withdrawMoneyUseCase;
    private final TransferRepository transferRepository;

    public Mono<Transfer> transfer(
            Long originAccountNumber, Long destinationAccountNumber, BigDecimal amount) {

        return withdrawMoneyUseCase.withdraw(originAccountNumber, amount)
                .zipWith(depositMoneyUseCase.deposit(destinationAccountNumber, amount))
                .flatMap(values -> {
                    Transaction transactionWithdraw = values.getT1();
                    Transaction transactionDeposit = values.getT2();

                    return transferRepository.save(
                            Transfer.builder().deposit(transactionDeposit).withdraw(transactionWithdraw).build());
                });
    }
}
