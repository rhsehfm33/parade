package ms.parade.domain.point;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserPointService {
    private final UserPointRepository userPointRepository;

    @Transactional
    public void changeUserPoint(long userId, long amount, PointType pointType) {
        if (PointType.SPEND.equals(pointType)) {
            userPointRepository.addPoint(userId, -amount);
        } else if (PointType.CHARGE.equals(pointType)) {
            userPointRepository.addPoint(userId, amount);
        } else {
            throw new IllegalArgumentException("ILLEGAL_POINT_TYPE; 잘못된 충전 타입입니다.");
        }
    }

    @Transactional
    public UserPoint getUserPoint(long userId) {
        return userPointRepository.findByUserId(userId).orElseThrow(
            () -> new EntityNotFoundException("USER_NOT_EXIST; 해당 사용자의 계좌가 존재하지 않습니다.")
        );
    }
}
