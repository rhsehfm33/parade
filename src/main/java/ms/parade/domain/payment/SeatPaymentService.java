package ms.parade.domain.payment;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class SeatPaymentService {
    private final SeatPaymentRepository seatPaymentRepository;

    public SeatPayment createSeatPayment(SeatPaymentCommand seatPaymentCommand) {
        return seatPaymentRepository.save(seatPaymentCommand.seatPaymentParams());
    }
}
