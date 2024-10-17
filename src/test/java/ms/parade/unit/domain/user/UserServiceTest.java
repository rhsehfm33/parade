package ms.parade.unit.domain.user;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ms.parade.domain.point.PointType;
import ms.parade.domain.user.User;
import ms.parade.domain.user.UserRepository;
import ms.parade.domain.user.UserService;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserService userService;

    // test data
    User user;

    @BeforeEach
    void setUp() {
        user = new User(1L, "test@gmail.com", "test", 100);
    }

    @Test
    public void updatePoint_BelowZero_IllegalArgumentException() {
        when(userRepository.findById(user.id())).thenReturn(Optional.of(user));

        assertThrows(IllegalArgumentException.class,
            () -> userService.updatePoint(user.id(), 101, PointType.SPEND));
    }

    @Test
    public void updatePoint_ToZero_0Point() {
        when(userRepository.findById(user.id())).thenReturn(Optional.of(user));

        User updatedUser = new User(user.id(), user.email(), user.password(), 0);
        when(userRepository.addPoint(user.id(), -user.point())).thenReturn(updatedUser);

        User actualUser = userService.updatePoint(user.id(), user.point(), PointType.SPEND);
        Assertions.assertEquals(updatedUser.point(), actualUser.point());
    }

    @Test
    public void findById_NotExist_EmptyOptional() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        Optional<User> actualUser = userService.findById(user.id());
        Assertions.assertTrue(actualUser.isEmpty());
    }
}
