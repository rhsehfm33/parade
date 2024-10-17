package ms.parade.interfaces.seat;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import ms.parade.domain.seat.Seat;
import ms.parade.domain.seat.SeatService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1")
public class SeatController {
    private final SeatService seatService;

    @GetMapping("/concerts/{scheduleId}/available-seats")
    ResponseEntity<List<SeatResponse>> getAvailableSeats(@PathVariable long scheduleId) {
        List<Seat> seats = seatService.findAvailableSeats(scheduleId);
        List<SeatResponse> seatResponses = seats.stream().map(SeatResponse::new).toList();
        return ResponseEntity.ok(seatResponses);
    }
}
