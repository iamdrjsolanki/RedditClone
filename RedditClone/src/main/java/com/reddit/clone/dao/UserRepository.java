package com.reddit.clone.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.reddit.clone.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
	Optional<User> findByUsername(String username);
	
}
