package ms.parade.interfaces.concert;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import ms.parade.domain.concert.ConcertScheduleInfo;
import ms.parade.domain.concert.ConcertService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1")
public class ConcertController {
    private final ConcertService concertService;

    @GetMapping("/protected/concerts/{concertId}/available-dates")
    ResponseEntity<List<ConcertResponse>> availableDates(@PathVariable long concertId) {
        List<ConcertScheduleInfo> concertScheduleInfos = concertService.findConcertSchedulesByConcertId(concertId);
        List<ConcertResponse> concertResponses = concertScheduleInfos.stream()
            .map(ConcertResponse::new).toList();
        return ResponseEntity.ok(concertResponses);
    }

}
