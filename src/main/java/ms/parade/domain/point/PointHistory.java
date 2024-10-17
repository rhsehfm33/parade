package ms.parade.domain.point;

public record PointHistory(
    long id,
    long userId,
    PointType type,
    long amount,
    String reason
) {
}
