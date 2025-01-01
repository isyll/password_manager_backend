package com.isyll.password_manager.repos;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.isyll.password_manager.models.EPermission;
import com.isyll.password_manager.models.Permission;

@Repository
public interface PermissionRepository extends CrudRepository<Permission, Long> {

    Optional<Permission> findByName(EPermission name);

}
