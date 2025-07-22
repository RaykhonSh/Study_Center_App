package uz.pdp.studycenter_app.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.studycenter_app.entity.Users;

import java.util.Optional;

public interface UsersRepository extends JpaRepository<Users, Integer> {
  Optional<Users> findByEmail(String email);
}