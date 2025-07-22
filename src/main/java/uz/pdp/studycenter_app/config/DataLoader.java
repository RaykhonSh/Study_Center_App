package uz.pdp.studycenter_app.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import uz.pdp.studycenter_app.entity.RoleName;
import uz.pdp.studycenter_app.entity.Roles;
import uz.pdp.studycenter_app.repo.RolesRepository;

import java.util.ArrayList;
import java.util.List;

@Component
public class DataLoader implements CommandLineRunner {
    private final RolesRepository rolesRepository;

    public DataLoader(RolesRepository rolesRepository) {
        this.rolesRepository = rolesRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (rolesRepository.count()==0){
            Roles role1 = Roles.builder().roleName(RoleName.ROLE_ADMIN).build();
            Roles role2 = Roles.builder().roleName(RoleName.ROLE_STUDENT).build();
            rolesRepository.saveAll(new ArrayList<>(List.of(role1, role2)));
        }
    }
}
