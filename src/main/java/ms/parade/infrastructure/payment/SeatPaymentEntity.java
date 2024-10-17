package ms.parade.infrastructure.payment;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ms.parade.domain.payment.SeatPayment;

@Entity
@Getter
@Table(name = "seat_payments")
@NoArgsConstructor
public class SeatPaymentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private long userId;

    private long seatReservationId;

    private String content;

    public static SeatPaymentEntity from(SeatPaymentParams seatPaymentParams) {
        SeatPaymentEntity seatPaymentEntity = new SeatPaymentEntity();
        seatPaymentEntity.userId = seatPaymentParams.userId();
        seatPaymentEntity.seatReservationId = seatPaymentParams.seatReservationId();
        seatPaymentEntity.content = seatPaymentParams.content();
        return seatPaymentEntity;
    }

    public static SeatPayment to(SeatPaymentEntity seatPaymentEntity) {
        return new SeatPayment(
            seatPaymentEntity.id,
            seatPaymentEntity.userId,
            seatPaymentEntity.seatReservationId,
            seatPaymentEntity.content
        );
    }
}
