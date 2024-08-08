package com.booklibrary.LibraryManagementSystem.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.booklibrary.LibraryManagementSystem.Data.Entities.PatronRoles;

@Repository
public interface PatronRolesRepository extends CrudRepository<PatronRoles, PatronRoles> {


    @Query(value = "SELECT * FROM patron_roles WHERE patron_id=?1 AND role_id=?2 LIMIT 1",
         nativeQuery = true)
    Optional<PatronRoles> findByUserIdAndRoleId(String patronId, String roleId);

    @Query(value = "SELECT * FROM patron_roles where patron_id=?1", nativeQuery = true)
    List<PatronRoles> findUserRoles(String userId);

    @Query(value = "SELECT normalized_role_name FROM roles INNER JOIN user_roles on user_id=?1",
         nativeQuery = true)
    List<String> findUserRolesName(String userId);

    @Query(value = "SELECT * FROM patron_roles WHERE patron_id=?1 AND role_id=?2 LIMIT 1",
          nativeQuery = true)
     Optional<PatronRoles> isPatronInRole(String patronId, String roleId);

}
