package com.reddit.clone.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.reddit.clone.dao.CommentRepository;
import com.reddit.clone.dao.PostRepository;
import com.reddit.clone.dao.UserRepository;
import com.reddit.clone.dto.CommentDto;
import com.reddit.clone.exception.PostNotFoundException;
import com.reddit.clone.exception.UsernameNotFoundException;
import com.reddit.clone.mappers.CommentMapper;
import com.reddit.clone.model.NotificationEmail;
import com.reddit.clone.model.Post;
import com.reddit.clone.model.User;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class CommentService {
	
	private static final String POST_URL = "";
	
	private final CommentRepository commentRepo;
	private final PostRepository postRepo;
	private final UserRepository userRepo;
	private final AuthService authService;
	private final CommentMapper commentMapper;
	private final MailContentBuilder mailContentBuilder;
	private final MailService mailService;

	public void createComments(CommentDto commentDto) {
		Post post = postRepo.findById(commentDto.getPostId())
						.orElseThrow(() -> new PostNotFoundException("Unable to find the post with id " + commentDto.getPostId()));
		commentRepo.save(
				commentMapper.map(commentDto, post, authService.getCurrentUser())
			);
		String message = mailContentBuilder.build(post.getUser().getUsername() + " posted a comment on your post " + POST_URL);
		//sendCommentNotification(message, authService.getCurrentUser());
	}

	private void sendCommentNotification(String message, User user) {
		mailService.sendMail(new NotificationEmail(user.getUsername(), "Commented on your post" + user.getEmail(), message));
	}

	public List<CommentDto> getCommentsForPost(Long postId) {
		Post post = postRepo.findById(postId)
						.orElseThrow(() -> new PostNotFoundException("Unable to find the post with id " + postId));
		
		return commentRepo.findByPost(post)
					.stream()
					.map(commentMapper::mapToDto)
					.collect(Collectors.toList());
	}

	public List<CommentDto> getCommentsByUser(String username) {
		User user = userRepo.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("Unable to find the user " + username));
		
		return commentRepo.findAllByUser(user)
				.stream()
				.map(commentMapper::mapToDto)
				.collect(Collectors.toList());
	}

}
