package com.upiiz.security.repositories;

import com.upiiz.security.entities.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    //Requerimos otros metodo
    Optional<User> findByUsername(String username);

}

