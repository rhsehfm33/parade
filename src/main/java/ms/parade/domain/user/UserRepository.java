package ms.parade.domain.user;

import java.util.Optional;

import ms.parade.infrastructure.user.UserParams;

public interface UserRepository {
    Optional<User> findById(long id);
    User save(UserParams userParams);
    User addPoint(long id, long amount);
}
