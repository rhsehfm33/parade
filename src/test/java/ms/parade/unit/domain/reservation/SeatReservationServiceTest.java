package ms.parade.unit.domain.reservation;

import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ms.parade.domain.reservation.ReservationStatus;
import ms.parade.domain.reservation.SeatReservation;
import ms.parade.domain.reservation.SeatReservationRepository;
import ms.parade.domain.reservation.SeatReservationService;

@ExtendWith(MockitoExtension.class)
public class SeatReservationServiceTest {
    @Mock
    SeatReservationRepository seatReservationRepository;

    @InjectMocks
    SeatReservationService seatReservationService;

    @Test
    public void findTimeoutReservationsForUpdate_PAYINGBefore5Minutes_Success() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime higherBound = now.minusMinutes(4).minusSeconds(59);
        LocalDateTime lowerBound = now.minusMinutes(5).minusSeconds(1);
        when(seatReservationRepository.findByStatusAndCreatedAtBeforeForUpdate(
            argThat(status -> status.equals(ReservationStatus.PAYING)),
            argThat(time -> time.isBefore(higherBound) && time.isAfter(lowerBound)))
        ).thenReturn(
            List.of(new SeatReservation(100, 1, 1, ReservationStatus.PAYING, LocalDateTime.now()))
        );

        List<SeatReservation> seatReservations = seatReservationService.findTimeoutReservationsForUpdate();
        Assertions.assertEquals(1, seatReservations.size());
        Assertions.assertEquals(100, seatReservations.get(0).id());
    }
}
