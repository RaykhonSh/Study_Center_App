package uz.pdp.studycenter_app.service;

import org.springframework.stereotype.Service;
import uz.pdp.studycenter_app.entity.RoleName;
import uz.pdp.studycenter_app.entity.Roles;
import uz.pdp.studycenter_app.repo.RolesRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class RoleService {

    private final RolesRepository rolesRepository;

    public RoleService(RolesRepository rolesRepository) {
        this.rolesRepository = rolesRepository;
    }

    public List<Roles> getSimpleRoles() {
        Roles role = rolesRepository.findByRoleName(RoleName.ROLE_STUDENT);
        return new ArrayList<>(List.of(role));
    }
}
