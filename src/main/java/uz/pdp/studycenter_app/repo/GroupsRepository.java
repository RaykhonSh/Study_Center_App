package uz.pdp.studycenter_app.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.studycenter_app.entity.Groups;

import java.util.Optional;

public interface GroupsRepository extends JpaRepository<Groups, Integer> {
    Optional<Groups> findByName(String name);
}