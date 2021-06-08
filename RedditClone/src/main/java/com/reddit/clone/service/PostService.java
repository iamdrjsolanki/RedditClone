package com.reddit.clone.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.reddit.clone.dao.PostRepository;
import com.reddit.clone.dao.SubredditRepository;
import com.reddit.clone.dao.UserRepository;
import com.reddit.clone.dto.PostRequest;
import com.reddit.clone.dto.PostResponse;
import com.reddit.clone.exception.PostNotFoundException;
import com.reddit.clone.exception.SpringRedditException;
import com.reddit.clone.exception.SubredditNotFoundException;
import com.reddit.clone.mappers.PostMapper;
import com.reddit.clone.model.Post;
import com.reddit.clone.model.Subreddit;
import com.reddit.clone.model.User;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class PostService {
	
	private final PostRepository postRepo;
	private final PostMapper postMapper;
	private final SubredditRepository subredditRepo;
	private final AuthService authService;
	private final UserRepository userRepo;
	
	public void save(PostRequest postReq) {
		Subreddit subreddit = subredditRepo.findByName(postReq.getSubredditName())
			.orElseThrow(() -> new SubredditNotFoundException("Unable to find subreddit " + postReq.getSubredditName()));
		
		postRepo.save(
				postMapper.map(postReq, subreddit, authService.getCurrentUser())
			);
	}

	@Transactional(readOnly = true)
	public PostResponse getPost(Long id) {
		Post post = postRepo.findById(id)
						.orElseThrow(() -> new PostNotFoundException("Unable to find the post with id "+ id));
		return postMapper.mapToDto(post);
	}

	@Transactional(readOnly = true)
	public List<PostResponse> getAllPosts() {
		return postRepo.findAll()
					.stream()
					.map(postMapper::mapToDto)
					.collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	public List<PostResponse> getPostsBySubreddit(Long id) {
		Subreddit subreddit = subredditRepo.findById(id)
				.orElseThrow(() -> new SubredditNotFoundException("Unable to find subreddit with id " + id));
		List<Post> posts = postRepo.findAllBySubreddit(subreddit);
		
		return posts.stream().map(postMapper::mapToDto).collect(Collectors.toList());
	}

	public List<PostResponse> getPostsByUsername(String username) {
		User user = userRepo.findByUsername(username)
						.orElseThrow(() -> new SpringRedditException("Unable to find the user "+ username));
		
		return postRepo.findByUser(user)
				.stream()
				.map(postMapper::mapToDto)
				.collect(Collectors.toList());
	}

}
