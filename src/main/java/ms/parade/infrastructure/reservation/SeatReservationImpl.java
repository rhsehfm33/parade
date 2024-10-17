package ms.parade.infrastructure.reservation;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import ms.parade.domain.reservation.ReservationStatus;
import ms.parade.domain.reservation.SeatReservation;
import ms.parade.domain.reservation.SeatReservationRepository;

@Repository
@RequiredArgsConstructor
public class SeatReservationImpl implements SeatReservationRepository {
    private final SeatReservationJpaRepository seatReservationJpaRepository;

    @Override
    public SeatReservation save(SeatReservationParams seatReservationParams) {
        SeatReservationEntity seatReservationEntity = SeatReservationEntity.from(seatReservationParams);
        seatReservationEntity = seatReservationJpaRepository.save(seatReservationEntity);
        return SeatReservationEntity.to(seatReservationEntity);
    }

    @Override
    public SeatReservation updateStatus(long seatId, ReservationStatus reservationStatus) {
        SeatReservationEntity seatReservationEntity = seatReservationJpaRepository.findById(seatId).orElseThrow(
            () -> new IllegalStateException("좌석[" + seatId + "]에 대한 예약이 존재하지 않습니다.")
        );
        seatReservationEntity.setStatus(reservationStatus);
        seatReservationEntity = seatReservationJpaRepository.save(seatReservationEntity);
        return SeatReservationEntity.to(seatReservationEntity);
    }

    @Override
    public Optional<SeatReservation> findByIdForUpdate(long id) {
        return seatReservationJpaRepository.findByIdForUpdate(id).map(SeatReservationEntity::to);
    }

    @Override
    public List<SeatReservation> findByStatusAndCreatedAtBeforeForUpdate(ReservationStatus status, LocalDateTime limit) {
        List<SeatReservationEntity> seatReservationEntities = seatReservationJpaRepository.findByStatusAndCreatedAtBefore(status, limit);
        return seatReservationEntities.stream().map(SeatReservationEntity::to).toList();
    }
}
