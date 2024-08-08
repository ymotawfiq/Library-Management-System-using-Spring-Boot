package com.booklibrary.LibraryManagementSystem.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.booklibrary.LibraryManagementSystem.Data.Entities.Roles;

@Repository
public interface RolesRepository extends CrudRepository<Roles, String> {


    @Query(value = "SELECT * FROM roles where normalized_role_name=?1", 
        nativeQuery =  true)
    Optional<Roles> findByRoleName(String roleName);

    @Query(value = "SELECT count(role) FROM roles", nativeQuery =  true)
    int findNumberOfRoles();

}
