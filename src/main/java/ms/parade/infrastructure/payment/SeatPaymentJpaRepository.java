package ms.parade.infrastructure.payment;

import org.springframework.data.jpa.repository.JpaRepository;


public interface SeatPaymentJpaRepository extends JpaRepository<SeatPaymentEntity, Long> {
}
