package com.peter.topset.Users.Repository;

import com.peter.topset.Users.Entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<RoleEntity,Long> {

    Optional<RoleEntity>findByName(String name);

}
