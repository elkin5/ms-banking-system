package co.elkin.veritran.usecase.transfer;

import co.elkin.veritran.model.transfer.Transfer;
import co.elkin.veritran.model.transfer.gateways.TransferRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class CreateTransferUseCase {
    private final TransferRepository transferRepository;

    public Mono<Transfer> save(Transfer transfer) {
        return transferRepository.save(transfer);
    }
}
