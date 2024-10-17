package ms.parade.infrastructure.reservation;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ms.parade.domain.reservation.ReservationStatus;
import ms.parade.domain.reservation.SeatReservation;

@Entity
@Getter
@Table(name = "seat_reservations")
@NoArgsConstructor
public class SeatReservationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private long userId;

    private long seatId;

    @Setter
    @Enumerated(EnumType.STRING)
    private ReservationStatus status;

    private LocalDateTime createdAt;

    public static SeatReservationEntity from(SeatReservationParams seatReservationParams) {
        SeatReservationEntity seatReservationEntity = new SeatReservationEntity();
        seatReservationEntity.userId = seatReservationParams.userId();
        seatReservationEntity.seatId = seatReservationParams.seatId();
        seatReservationEntity.status = seatReservationParams.status();
        seatReservationEntity.createdAt = seatReservationParams.createdAt();
        return seatReservationEntity;
    }

    public static SeatReservation to(SeatReservationEntity seatReservationEntity) {
        return new SeatReservation(
            seatReservationEntity.id,
            seatReservationEntity.userId,
            seatReservationEntity.seatId,
            seatReservationEntity.status,
            seatReservationEntity.createdAt
        );
    }
}
