package uz.pdp.studycenter_app.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.studycenter_app.entity.RoleName;
import uz.pdp.studycenter_app.entity.Roles;

public interface RolesRepository extends JpaRepository<Roles, Integer> {
  Roles findByRoleName(RoleName roleName);
}