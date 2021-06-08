package com.reddit.clone.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.reddit.clone.model.Comment;
import com.reddit.clone.model.Post;
import com.reddit.clone.model.User;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    
	List<Comment> findByPost(Post post);
	
    List<Comment> findAllByUser(User user);
    
}
