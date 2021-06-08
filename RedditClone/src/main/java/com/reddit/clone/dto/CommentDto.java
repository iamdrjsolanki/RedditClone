package com.reddit.clone.dto;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {
	
	private Long id;
	private Long postId;
	private Instant createdDate;
	private String Text;
	private String userName;

}
