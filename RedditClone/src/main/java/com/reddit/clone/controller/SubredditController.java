package com.reddit.clone.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.reddit.clone.dto.SubredditDto;
import com.reddit.clone.service.SubredditService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/subreddit")
@AllArgsConstructor
@Slf4j
public class SubredditController {
	
	private final SubredditService subredditService;
	
	@PostMapping
	public ResponseEntity<?> createSubreddit(@RequestBody SubredditDto subredditdto) {
		return new ResponseEntity<>(subredditService.save(subredditdto), HttpStatus.CREATED);
	}
	
	@GetMapping
	public ResponseEntity<?> getAllSubreddits() {
		return new ResponseEntity<>(subredditService.getAllSubreddits(), HttpStatus.OK);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<SubredditDto> getSubreddit(@PathVariable("id") Long id) {
		return new ResponseEntity<SubredditDto>(subredditService.getSubreddit(id), HttpStatus.OK);
	}

}
