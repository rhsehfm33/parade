package ms.parade.infrastructure.concert;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ms.parade.domain.concert.ConcertSchedule;

@Entity
@Getter
@Table(name = "concert_schedules")
@NoArgsConstructor
public class ConcertScheduleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private long concertId;

    private int allSeats;

    @Setter
    private int availableSeats;

    private LocalDate performanceDate;

    public static ConcertSchedule to(ConcertScheduleEntity concertScheduleEntity) {
        return new ConcertSchedule(
            concertScheduleEntity.id,
            concertScheduleEntity.concertId,
            concertScheduleEntity.allSeats,
            concertScheduleEntity.availableSeats,
            concertScheduleEntity.performanceDate
        );
    }
}
