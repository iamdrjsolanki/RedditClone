package com.reddit.clone.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.reddit.clone.dto.CommentDto;
import com.reddit.clone.service.CommentService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/comments")
@AllArgsConstructor
public class CommentsController {
	
	private final CommentService commentService;
	
	@PostMapping
	public ResponseEntity<Void> createComments(@RequestBody CommentDto commentDto) {
		commentService.createComments(commentDto);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}
	
	@GetMapping("/by-postId/{postId}")
	public ResponseEntity<List<CommentDto>> getAllCommentsForPost(@PathVariable("postId") Long postId) {
		return new ResponseEntity<>(commentService.getCommentsForPost(postId), HttpStatus.OK);
	}
	
	@GetMapping("/by-user/{username}")
	public ResponseEntity<List<CommentDto>> getAllCommentsByUser(@PathVariable("username") String username) {
		return new ResponseEntity<>(commentService.getCommentsByUser(username), HttpStatus.OK);
	}

}
