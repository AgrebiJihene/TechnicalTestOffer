package com.example.user.repositories;

import com.example.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Dealing with CRUD operations
 * Used for DAO implementation to handle database communication
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
