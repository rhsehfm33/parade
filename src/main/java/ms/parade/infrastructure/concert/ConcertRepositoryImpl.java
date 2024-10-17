package ms.parade.infrastructure.concert;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import ms.parade.domain.concert.ConcertRepository;
import ms.parade.domain.concert.ConcertSchedule;

@Repository
@RequiredArgsConstructor
public class ConcertRepositoryImpl implements ConcertRepository {
    private final ConcertScheduleJpaRepository concertScheduleJpaRepository;

    @Override
    public List<ConcertSchedule> findSchedulesByConcertId(long concertId) {
        List<ConcertScheduleEntity> concertScheduleEntities = concertScheduleJpaRepository.findByConcertId(concertId);

        return concertScheduleEntities.stream().map(ConcertScheduleEntity::to).toList();
    }

    @Override
    public Optional<ConcertSchedule> findScheduleByIdForUpdate(long id) {
        return concertScheduleJpaRepository.findByIdForUpdate(id).map(ConcertScheduleEntity::to);
    }

    @Override
    public ConcertSchedule updateScheduleAvailableSeats(long id, int availableSeats) {
        ConcertScheduleEntity concertScheduleEntity = concertScheduleJpaRepository.findByIdForUpdate(id)
            .orElseThrow(() -> new EntityNotFoundException("해당 콘서트 스케쥴은 존재하지 않습니다."));
        concertScheduleEntity.setAvailableSeats(availableSeats);
        concertScheduleEntity = concertScheduleJpaRepository.save(concertScheduleEntity);
        return ConcertScheduleEntity.to(concertScheduleEntity);
    }

    @Override
    public ConcertSchedule saveSchedule(ConcertScheduleParams concertScheduleParams) {
        ConcertScheduleEntity concertScheduleEntity = ConcertScheduleEntity.from(concertScheduleParams);
        concertScheduleEntity = concertScheduleJpaRepository.save(concertScheduleEntity);
        return ConcertScheduleEntity.to(concertScheduleEntity);
    }
}
