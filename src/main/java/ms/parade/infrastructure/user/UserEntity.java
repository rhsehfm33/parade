package ms.parade.infrastructure.user;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ms.parade.domain.user.User;

@Entity
@Getter
@Table(name = "users")
@NoArgsConstructor
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String email;

    private String password;

    @Setter
    private long point;

    public static UserEntity from(UserParams userParams) {
        UserEntity userEntity = new UserEntity();
        userEntity.email = userParams.email();
        userEntity.password = userParams.password();
        userEntity.point = 0;
        return userEntity;
    }

    public static User to(UserEntity entity) {
        return new User(
            entity.getId(),
            entity.getEmail(),
            entity.getPassword(),
            entity.getPoint()
        );
    }
}
