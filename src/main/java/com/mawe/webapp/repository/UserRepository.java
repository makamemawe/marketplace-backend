package com.mawe.webapp.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mawe.webapp.entity.User;
import com.mawe.webapp.enums.UserRole;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findFirstByEmail(String email);

    User findByRole(UserRole userRole);

}
