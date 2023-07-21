package com.example.doctorappointment.repository;

import com.example.doctorappointment.model.User;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsUserByEmailAddressOrUserName(String emailAddress,String username);
    Optional<User> findUserByUserName(String username);
    @Override
    @CacheEvict(value = "users", allEntries = true)
    <S extends User> S save(S entity);
}
