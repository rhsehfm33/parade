package ms.parade.interfaces.payment;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import ms.parade.application.payment.PaymentFacade;
import ms.parade.domain.payment.SeatPayment;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1")
public class PaymentController {
    private final PaymentFacade paymentFacade;

    @PostMapping("/protected/seat-reservations/payment")
    ResponseEntity<SeatPayment> paySeatReservation(
        Authentication authentication,
        @RequestBody SeatPaymentRequest seatPaymentRequest
    ) {
        return ResponseEntity.ok(
            paymentFacade.payForSeat((long)authentication.getPrincipal(), seatPaymentRequest.reservationId())
        );
    }
}
