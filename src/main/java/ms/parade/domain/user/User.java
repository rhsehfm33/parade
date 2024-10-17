package ms.parade.domain.user;

public record User(
    long id,
    String email,
    String password,
    long point
) {
}
