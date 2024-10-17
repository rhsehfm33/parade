package ms.parade.infrastructure.payment;

import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import ms.parade.domain.payment.SeatPayment;
import ms.parade.domain.payment.SeatPaymentRepository;

@Repository
@RequiredArgsConstructor
public class SeatPaymentRepositoryImpl implements SeatPaymentRepository {
    private final SeatPaymentJpaRepository seatPaymentJpaRepository;

    @Override
    public SeatPayment save(SeatPaymentParams seatPaymentParams) {
        SeatPaymentEntity seatPaymentEntity = SeatPaymentEntity.from(seatPaymentParams);
        seatPaymentEntity = seatPaymentJpaRepository.save(seatPaymentEntity);
        return SeatPaymentEntity.to(seatPaymentEntity);
    }
}
