package ms.parade.infrastructure.user;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import ms.parade.domain.user.User;
import ms.parade.domain.user.UserRepository;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {
    private final UserJpaRepository userJpaRepository;

    @Override
    public Optional<User> findById(long id) {
        return userJpaRepository.findById(id).map(UserEntity::to);
    }

    @Override
    public User save(UserParams userParams) {
        UserEntity userEntity = UserEntity.from(userParams);
        userEntity = userJpaRepository.save(userEntity);
        return UserEntity.to(userEntity);
    }

    @Override
    public User addPoint(long id, long amount) {
        UserEntity userEntity = userJpaRepository.findById(id).orElseThrow(
            () -> new IllegalArgumentException("해당 사용자가 존재하지 않습니다")
        );
        userEntity.setPoint(userEntity.getPoint() + amount);
        userEntity = userJpaRepository.save(userEntity);
        return UserEntity.to(userEntity);
    }
}
