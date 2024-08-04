package site.ph0en1x.task_management_sys.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.ph0en1x.task_management_sys.model.user.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

}
