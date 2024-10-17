package ms.parade.domain.payment;

import ms.parade.infrastructure.payment.SeatPaymentParams;

public interface SeatPaymentRepository {
    SeatPayment save(SeatPaymentParams seatPaymentParams);
}
