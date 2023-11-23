package aheung.likelion.twoneone.repository;

import aheung.likelion.twoneone.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
