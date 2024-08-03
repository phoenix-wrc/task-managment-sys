package site.ph0en1x.task_management_sys.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.ph0en1x.task_management_sys.model.user.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
