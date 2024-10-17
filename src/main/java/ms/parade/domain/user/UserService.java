package ms.parade.domain.user;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import ms.parade.domain.point.PointType;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User updatePoint(long userId, long amount, PointType pointType) {
        User user = userRepository.findById(userId).orElseThrow(
            () -> new EntityNotFoundException("존재하지 않는 사용자입니다.")
        );

        if (PointType.CHARGE.equals(pointType)) {
            return userRepository.addPoint(userId, amount);
        } else if (PointType.SPEND.equals(pointType)) {
            if (user.point() - amount < 0) {
                throw new IllegalArgumentException("잔액이 부족합니다.");
            }
            return userRepository.addPoint(userId, -amount);
        } else {
            throw new IllegalArgumentException("잘못된 충전 타입입니다.");
        }
    }

    public Optional<User> findById(long userId) {
        return userRepository.findById(userId);
    }
}
