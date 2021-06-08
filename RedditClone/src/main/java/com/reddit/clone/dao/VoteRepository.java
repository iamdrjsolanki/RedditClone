package com.reddit.clone.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.reddit.clone.model.Post;
import com.reddit.clone.model.User;
import com.reddit.clone.model.Vote;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {
    
	Optional<Vote> findTopByPostAndUserOrderByVoteIdDesc(Post post, User currentUser);
	
}
