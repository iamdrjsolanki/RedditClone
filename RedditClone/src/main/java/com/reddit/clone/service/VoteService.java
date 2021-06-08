package com.reddit.clone.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.reddit.clone.dao.PostRepository;
import com.reddit.clone.dao.VoteRepository;
import com.reddit.clone.dto.VoteDto;
import com.reddit.clone.exception.PostNotFoundException;
import com.reddit.clone.exception.SpringRedditException;
import com.reddit.clone.model.Post;
import com.reddit.clone.model.Vote;
import com.reddit.clone.model.VoteType;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
@Transactional
public class VoteService {
	
	private final PostRepository postRepo;
	private final VoteRepository voteRepo;
	private final AuthService authServcie;
	
	public void vote(VoteDto voteDto) {
		Post post = postRepo.findById(voteDto.getPostId())
			.orElseThrow(() -> new PostNotFoundException("Unable to find post with id " + voteDto.getPostId()));
		
		Optional<Vote> voteByPostAndUser = voteRepo.findTopByPostAndUserOrderByVoteIdDesc(post, authServcie.getCurrentUser());
		//if the current user is trying to do the same vote on the same post again.
		if(voteByPostAndUser.isPresent() && voteByPostAndUser.get().getVoteType().equals(voteDto.getVoteType())) {
			throw new SpringRedditException(authServcie.getCurrentUser().getUsername() + 
					" you have already " + voteDto.getVoteType() + 
					"D this post."
				);
		}
		if(VoteType.UPVOTE.equals(voteDto.getVoteType())) {
			post.setVoteCount(post.getVoteCount() + 1);
		} else {
			post.setVoteCount(post.getVoteCount() - 1);
		}
		
		voteRepo.save(mapToVote(voteDto, post));
		postRepo.save(post);
	}
	
	private Vote mapToVote(VoteDto voteDto, Post post) {
		return Vote.builder()
					.voteType(voteDto.getVoteType())
					.post(post)
					.user(authServcie.getCurrentUser())
					.build();
	}

}
