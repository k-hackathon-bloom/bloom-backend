package com.example.bloombackend.bottlemsg.sevice;

import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.bloombackend.bottlemsg.controller.dto.request.CreateBottleMessageReactionRequest;
import com.example.bloombackend.bottlemsg.controller.dto.response.BottleMessageReactionResponse;
import com.example.bloombackend.bottlemsg.entity.BottleMessageReaction;
import com.example.bloombackend.bottlemsg.entity.ReactionType;
import com.example.bloombackend.bottlemsg.repository.BottleMessageReactionRepository;
import com.example.bloombackend.user.entity.UserEntity;

@Service
public class ReactionService {
	private final BottleMessageReactionRepository bottleMessageReactionRepository;

	public ReactionService(BottleMessageReactionRepository bottleMessageReactionRepository) {
		this.bottleMessageReactionRepository = bottleMessageReactionRepository;
	}

	@Transactional
	public BottleMessageReactionResponse updateBottleMessageReaction(Long messageId, Long userId,
		CreateBottleMessageReactionRequest request) {
		UserEntity reactor = userService.findUserById(userId);
		bottleMessageReactionRepository.save(
			BottleMessageReaction.builder()
				.reactor(reactor)
				.message(getBottleMessageEntity(messageId))
				.reactionType(ReactionType.valueOf(request.reaction()))
				.build());
		return getReactionCount(messageId, isReacted(userId, messageId));
	}

	private BottleMessageReactionResponse getReactionCount(Long messageId, boolean isReacted) {
		Map<ReactionType, Long> reactionsCountMap = bottleMessageReactionRepository.countReactionsByMessage(messageId);

		int likeCount = reactionsCountMap.getOrDefault(ReactionType.LIKE, 0L).intValue();
		int empathyCount = reactionsCountMap.getOrDefault(ReactionType.EMPATHY, 0L).intValue();
		int cheerCount = reactionsCountMap.getOrDefault(ReactionType.CHEER, 0L).intValue();

		return new BottleMessageReactionResponse(isReacted, likeCount, empathyCount, cheerCount);
	}

	private boolean isReacted(Long userId, Long messageId) {
		return bottleMessageReactionRepository.findByReactorIdAndMessageId(userId, messageId).isPresent();
	}
}
