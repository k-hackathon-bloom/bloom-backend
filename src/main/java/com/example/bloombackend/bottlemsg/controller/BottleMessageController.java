package com.example.bloombackend.bottlemsg.controller;

import com.example.bloombackend.bottlemsg.controller.dto.response.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.bloombackend.bottlemsg.controller.dto.request.CreateBottleMessageReactionRequest;
import com.example.bloombackend.bottlemsg.controller.dto.request.CreateBottleMessageRequest;
import com.example.bloombackend.bottlemsg.service.BottleMessageService;
import com.example.bloombackend.global.config.annotation.CurrentUser;

@RestController
@RequestMapping("/api/bottle-messages")
public class BottleMessageController {
	private final BottleMessageService bottleMessageService;

	public BottleMessageController(BottleMessageService bottleMessageService) {
		this.bottleMessageService = bottleMessageService;
	}

	@PostMapping("")
	public ResponseEntity<CreateBottleMessageResponse> createBottleMessage(
		@CurrentUser Long userId,
		@RequestBody CreateBottleMessageRequest request) {

		return ResponseEntity.ok(bottleMessageService.createBottleMessage(userId, request));
	}

	@GetMapping("")
	public ResponseEntity<ReceivedBottleMessagesResponse> getReceivedBottleMessages(@CurrentUser Long userId) {
		return ResponseEntity.ok(bottleMessageService.getReceivedBottleMessages(userId));
	}

	@GetMapping("/{messageId}")
	public ResponseEntity<BottleMessageDetailResponse> getDetailBottleMessage(@CurrentUser Long userId,
		@PathVariable("messageId") Long messageId) {
		return ResponseEntity.ok(bottleMessageService.getDetailBottleMessage(messageId, userId));
	}

	@GetMapping("/random")
	public ResponseEntity<BottleMessageDetailResponse> createBottleMessageRandom(@CurrentUser Long userId) {
		return ResponseEntity.ok(bottleMessageService.getRandomBottleMessage(userId));
	}

	@PostMapping("/{messageId}/react")
	public ResponseEntity<BottleMessageReactionResponse> reactBottleMessage(
		@CurrentUser Long userId,
		@RequestBody CreateBottleMessageReactionRequest request,
		@PathVariable("messageId") Long messageId) {
		return ResponseEntity.ok(bottleMessageService.updateBottleMessageReaction(messageId, userId, request));
	}

	@PostMapping("/{messageId}/delete")
	public ResponseEntity<ReceivedBottleMessagesResponse> deleteBottleMessage(
		@CurrentUser Long userId,
		@PathVariable("messageId") Long messageId) {
		return ResponseEntity.ok(bottleMessageService.deleteBottleMessage(userId, messageId));
	}

	@GetMapping("/sent")
	public ResponseEntity<SentBottleMessageResponse> sentBottleMessages(@CurrentUser Long userId) {
		return ResponseEntity.ok(bottleMessageService.getSentBottleMessages(userId));
	}

	@DeleteMapping("/{messageId}/react")
	public ResponseEntity<Void> deleteBottleMessageReaction(
		@CurrentUser Long userId,
		@PathVariable("messageId") Long messageId,
		@RequestBody CreateBottleMessageReactionRequest request
	) {
		bottleMessageService.deleteBottleMessageReaction(messageId, userId, request.reaction());
		return ResponseEntity.ok().build();
	}

	@PatchMapping("/{messageId}/hide")
	public ResponseEntity<Void> hideSentBottleMessage(
			@CurrentUser Long userId,
			@PathVariable("messageId") Long messageId) {
		bottleMessageService.hideSentMessage(messageId);
		return ResponseEntity.ok().build();
	}

	@GetMapping("/available")
	public ResponseEntity<IsAvailableSender> getIsAvailable(
			@CurrentUser Long userId
	) {
		return ResponseEntity.ok(bottleMessageService.getIsAvailableSender(userId));
	}
}
