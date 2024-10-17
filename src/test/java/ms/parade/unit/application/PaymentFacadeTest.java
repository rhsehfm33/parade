package ms.parade.unit.application;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import jakarta.persistence.EntityNotFoundException;
import ms.parade.application.payment.PaymentFacade;
import ms.parade.domain.payment.SeatPaymentService;
import ms.parade.domain.point.PointHistoryService;
import ms.parade.domain.point.PointType;
import ms.parade.domain.reservation.ReservationStatus;
import ms.parade.domain.reservation.SeatReservation;
import ms.parade.domain.reservation.SeatReservationService;
import ms.parade.domain.seat.Seat;
import ms.parade.domain.seat.SeatService;
import ms.parade.domain.seat.SeatStatus;
import ms.parade.domain.user.UserService;

@ExtendWith(MockitoExtension.class)
public class PaymentFacadeTest {
    @Mock
    SeatReservationService seatReservationService;

    @Mock
    PointHistoryService pointHistoryService;

    @Mock
    SeatService seatService;

    @Mock
    UserService userService;

    @Mock
    SeatPaymentService seatPaymentService;

    @InjectMocks
    PaymentFacade paymentFacade;

    @Test
    public void payForSeat_ReservationNotExist_EntityNotFoundException() {
        when(seatReservationService.findByIdForUpdate(1)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> paymentFacade.payForSeat(1, 1));
    }

    @Test
    public void payForSeat_UserNotMatchWithReservation_IllegalArgumentException() {
        SeatReservation seatReservation = new SeatReservation(1, 1, 1, null ,null);
        when(seatReservationService.findByIdForUpdate(1)).thenReturn(Optional.of(seatReservation));
        assertThrows(IllegalArgumentException.class, () -> paymentFacade.payForSeat(9, 1));
    }

    @Test
    public void payForSeat_NotPayingStatus_IllegalArgumentException() {
        SeatReservation seatReservation = new SeatReservation(1, 1, 1, ReservationStatus.CANCEL,null);
        when(seatReservationService.findByIdForUpdate(1)).thenReturn(Optional.of(seatReservation));
        assertThrows(IllegalArgumentException.class, () -> paymentFacade.payForSeat(1, 1));
    }

    @Test
    public void payForSeat_SeatNotBooked_IllegalArgumentException() {
        SeatReservation seatReservation = new SeatReservation(1, 1, 1, ReservationStatus.PAYING,null);
        when(seatReservationService.findByIdForUpdate(1)).thenReturn(Optional.of(seatReservation));

        Seat seat = new Seat(1, 1, "A1", 1_000_000, SeatStatus.EMPTY);
        when(seatService.findByIdForUpdate(1)).thenReturn(Optional.of(seat));

        assertThrows(IllegalArgumentException.class, () -> paymentFacade.payForSeat(1, 1));
        verify(seatReservationService).findByIdForUpdate(1);
        verify(seatService).findByIdForUpdate(1);
    }

    @Test
    public void payForSeat_Success() {
        SeatReservation seatReservation = new SeatReservation(1, 1, 1, ReservationStatus.PAYING,null);
        when(seatReservationService.findByIdForUpdate(1)).thenReturn(Optional.of(seatReservation));

        Seat seat = new Seat(1, 1, "A1", 1_000_00, SeatStatus.BOOKED);
        when(seatService.findByIdForUpdate(1)).thenReturn(Optional.of(seat));

        assertDoesNotThrow(() -> paymentFacade.payForSeat(1, 1));

        verify(seatReservationService).findByIdForUpdate(1);

        verify(seatService).findByIdForUpdate(1);

        verify(userService).updatePoint(1, 1_000_00, PointType.SPEND);

        verify(seatPaymentService).createSeatPayment(argThat(seatPaymentCommand ->
            seatPaymentCommand.seatPaymentParams().seatReservationId() == 1 &&
            seatPaymentCommand.seatPaymentParams().userId() == 1));

        verify(pointHistoryService).record(argThat(pointHistoryCommand ->
            pointHistoryCommand.pointHistoryParams().userId() == 1 &&
            PointType.SPEND.equals(pointHistoryCommand.pointHistoryParams().type()) &&
            pointHistoryCommand.pointHistoryParams().amount() == seat.price()
        ));
    }
}
