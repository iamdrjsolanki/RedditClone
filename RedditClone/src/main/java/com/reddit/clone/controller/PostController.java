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

import com.reddit.clone.dto.PostRequest;
import com.reddit.clone.dto.PostResponse;
import com.reddit.clone.service.PostService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/post")
@AllArgsConstructor
public class PostController {
	
	private final PostService postService;
	
	@PostMapping
	public ResponseEntity<Void> createPost(@RequestBody PostRequest post) {
		postService.save(post);
		return new ResponseEntity<Void>(HttpStatus.CREATED);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<PostResponse> getPost(@PathVariable("id") Long id) {
		return new ResponseEntity<>(postService.getPost(id), HttpStatus.OK);
	}
	
	@GetMapping()
	public ResponseEntity<List<PostResponse>> getAllPosts() {
		return new ResponseEntity<>(postService.getAllPosts(), HttpStatus.OK);
	}
	
	@GetMapping("/by-subreddit/{id}")
	public ResponseEntity<List<PostResponse>> getPostsBySubreddit(@PathVariable("id") Long id) {
		return new ResponseEntity<>(postService.getPostsBySubreddit(id), HttpStatus.OK);
	}
	
	@GetMapping("/by-user/{user}")
	public ResponseEntity<List<PostResponse>> getPostsByUsername(@PathVariable("user") String username) {
		return new ResponseEntity<>(postService.getPostsByUsername(username), HttpStatus.OK);
	}

}
