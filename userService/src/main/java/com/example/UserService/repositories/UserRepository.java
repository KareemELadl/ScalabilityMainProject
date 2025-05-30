package com.example.UserService.repositories;

import com.example.UserService.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    // This interface will extend JpaRepository to provide CRUD operations for User entity
    // It will also allow us to define custom query methods if needed
    // For example, findByUsername, findByEmail, etc.
    // Spring Data JPA will automatically implement these methods based on the method names


    // get all users
     List<User> findAll();

    // find user by email
        User findByEmail(String email);

    // find user by userId
    User findByUserId(Long userId);

    // delete user by mail
    void deleteByEmail(String email);

}
