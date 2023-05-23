package com.ms.bap.dao;

import com.ms.bap.entity.Users;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsersRepo extends CrudRepository<Users, String> {

    Optional<Users> findByEmail(String username);

    Boolean existsByEmail(String email);
    Boolean existsByMobile(String mobile);

}
