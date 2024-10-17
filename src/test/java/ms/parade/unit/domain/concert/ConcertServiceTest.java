package ms.parade.unit.domain.concert;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import jakarta.persistence.EntityNotFoundException;
import ms.parade.domain.concert.ConcertRepository;
import ms.parade.domain.concert.ConcertSchedule;
import ms.parade.domain.concert.ConcertService;

@ExtendWith(MockitoExtension.class)
public class ConcertServiceTest {
    @Mock
    ConcertRepository concertRepository;

    @InjectMocks
    ConcertService concertService;

    @Test
    public void addAvailableSeats_MinusToUnder0_IllegalArgumentException() {
        ConcertSchedule concertSchedule = new ConcertSchedule(1, 1, 50, 2, LocalDate.now());
        when(concertRepository.findScheduleByIdForUpdate(concertSchedule.id()))
            .thenReturn(Optional.of(concertSchedule));
        assertThrows(IllegalArgumentException.class,
            () -> concertService.addAvailableSeats(concertSchedule.id(), -3));
    }

    @Test
    public void addAvailableSeats_MinusTo0_Success() {
        ConcertSchedule concertSchedule = new ConcertSchedule(1, 1, 50, 2, LocalDate.now());
        when(concertRepository.findScheduleByIdForUpdate(concertSchedule.id()))
            .thenReturn(Optional.of(concertSchedule));
        assertDoesNotThrow(() -> concertService.addAvailableSeats(concertSchedule.id(), -2));
        verify(concertRepository).updateScheduleAvailableSeats(concertSchedule.id(), 0);
    }

    @Test
    public void addAvailableSeats_AddOverMax_IllegalArgumentException() {
        ConcertSchedule concertSchedule = new ConcertSchedule(1, 1, 50, 1, LocalDate.now());
        when(concertRepository.findScheduleByIdForUpdate(concertSchedule.id()))
            .thenReturn(Optional.of(concertSchedule));
        assertThrows(IllegalArgumentException.class,
            () -> concertService.addAvailableSeats(concertSchedule.id(), 50));
    }

    @Test
    public void addAvailableSeats_AddToMax_Success() {
        ConcertSchedule concertSchedule = new ConcertSchedule(1, 1, 50, 1, LocalDate.now());
        when(concertRepository.findScheduleByIdForUpdate(concertSchedule.id()))
            .thenReturn(Optional.of(concertSchedule));
        assertDoesNotThrow(() -> concertService.addAvailableSeats(concertSchedule.id(), 49));
        verify(concertRepository).updateScheduleAvailableSeats(concertSchedule.id(), 50);
    }

    @Test
    public void addAvailableSeats_ScheduleNotExists_EntityNotFoundException() {
        when(concertRepository.findScheduleByIdForUpdate(1)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class,
            () -> concertService.addAvailableSeats(1, 50));
    }
}
