package ms.parade.infrastructure.seat;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ms.parade.domain.seat.Seat;
import ms.parade.domain.seat.SeatStatus;

@Entity
@Getter
@Table(name = "seats")
@NoArgsConstructor
public class SeatEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private long scheduleId;

    private String name;

    private long price;

    @Setter
    @Enumerated(EnumType.STRING)
    private SeatStatus status;

    @PrePersist
    public void prePersist() {
        if (status == null) {
            status = SeatStatus.EMPTY;
        }
    }

    public static SeatEntity from(SeatParams seatParams) {
        SeatEntity seatEntity = new SeatEntity();
        seatEntity.scheduleId = seatParams.scheduleId();
        seatEntity.name = seatParams.name();
        seatEntity.price = seatParams.price();
        seatEntity.status = seatParams.status();
        return seatEntity;
    }

    public static Seat to(SeatEntity seatEntity) {
        return new Seat(
            seatEntity.id,
            seatEntity.scheduleId,
            seatEntity.name,
            seatEntity.price,
            seatEntity.status
        );
    }
}
