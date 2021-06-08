package com.reddit.clone.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.reddit.clone.dao.SubredditRepository;
import com.reddit.clone.dto.SubredditDto;
import com.reddit.clone.exception.SpringRedditException;
import com.reddit.clone.mappers.SubredditMapper;
import com.reddit.clone.model.Subreddit;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class SubredditService {
	
	private final SubredditRepository subredditRepo;
	private final SubredditMapper subredditMapper;
	
	@Transactional
	public SubredditDto save(SubredditDto subredditDto) {
		Subreddit subredditObj = subredditMapper.mapDtoToSubreddit(subredditDto);
		Subreddit savedSubredditObj = subredditRepo.save(subredditObj);
		
		subredditDto.setSubredditId(savedSubredditObj.getSubredditId());
		return subredditDto;
	}

	@Transactional(readOnly = true)
	public List<SubredditDto> getAllSubreddits() {
		return subredditRepo.findAll()
				.stream()
				.map(subredditMapper::mapSubredditToDto)
				.collect(Collectors.toList());
	}

	public SubredditDto getSubreddit(Long id) {
		Subreddit subreddit = subredditRepo.findById(id)
									.orElseThrow(() -> new SpringRedditException("Unable to find the subreddit with id "+ id));
		return subredditMapper.mapSubredditToDto(subreddit);
	}

}
