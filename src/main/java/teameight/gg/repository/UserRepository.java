package teameight.gg.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import teameight.gg.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);
}
