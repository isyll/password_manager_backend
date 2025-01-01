package com.isyll.password_manager.repos;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.isyll.password_manager.models.ERole;
import com.isyll.password_manager.models.Role;

@Repository
public interface RoleRepository extends CrudRepository<Role, Long> {

    Role findByName(ERole name);

    Role save(ERole permission);

}
