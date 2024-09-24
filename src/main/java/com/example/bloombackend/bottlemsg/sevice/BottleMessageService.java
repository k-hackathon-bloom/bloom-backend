package com.example.bloombackend.bottlemsg.sevice;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.bloombackend.bottlemsg.controller.dto.request.CreateBottleMessageReactionRequest;
import com.example.bloombackend.bottlemsg.controller.dto.request.CreateBottleMessageRequest;
import com.example.bloombackend.bottlemsg.controller.dto.response.BottleMessageDetailResponse;
import com.example.bloombackend.bottlemsg.controller.dto.response.BottleMessageReactionResponse;
import com.example.bloombackend.bottlemsg.controller.dto.response.CreateBottleMessageResponse;
import com.example.bloombackend.bottlemsg.controller.dto.response.Info.BottleMessageLogInfo;
import com.example.bloombackend.bottlemsg.controller.dto.response.Info.BottleMessageSummaryInfo;
import com.example.bloombackend.bottlemsg.controller.dto.response.Info.BottleMessageWithDateLogInfo;
import com.example.bloombackend.bottlemsg.controller.dto.response.Info.SentBottleMessageInfo;
import com.example.bloombackend.bottlemsg.controller.dto.response.ReceivedBottleMessagesResponse;
import com.example.bloombackend.bottlemsg.controller.dto.response.RecentSentAtResponse;
import com.example.bloombackend.bottlemsg.controller.dto.response.SentBottleMessageResponse;
import com.example.bloombackend.bottlemsg.entity.BottleMessageEntity;
import com.example.bloombackend.bottlemsg.entity.BottleMessageReaction;
import com.example.bloombackend.bottlemsg.entity.BottleMessageReceiptLog;
import com.example.bloombackend.bottlemsg.entity.Negativity;
import com.example.bloombackend.bottlemsg.entity.ReactionType;
import com.example.bloombackend.bottlemsg.repository.BottleMessageLogRepository;
import com.example.bloombackend.bottlemsg.repository.BottleMessageReactionRepository;
import com.example.bloombackend.bottlemsg.repository.BottleMessageRepository;
import com.example.bloombackend.claude.dto.EmotionScore;
import com.example.bloombackend.claude.dto.SentimentAnalysisDto;
import com.example.bloombackend.claude.service.ClaudeService;
import com.example.bloombackend.user.entity.UserEntity;
import com.example.bloombackend.user.service.UserService;

@Service
public class BottleMessageService {
	private final BottleMessageRepository bottleMessageRepository;
	private final BottleMessageLogRepository bottleMessageLogRepository;
	private final UserService userService;
	private final BottleMessageReactionRepository bottleMessageReactionRepository;
	private final ClaudeService claudeService;

	@Autowired
	public BottleMessageService(BottleMessageRepository bottleMessageRepository,
		BottleMessageLogRepository bottleMessageLogRepository, UserService userService,
		BottleMessageReactionRepository bottleMessageReactionRepository, ClaudeService claudeService) {
		this.bottleMessageRepository = bottleMessageRepository;
		this.bottleMessageLogRepository = bottleMessageLogRepository;
		this.userService = userService;
		this.bottleMessageReactionRepository = bottleMessageReactionRepository;
		this.claudeService = claudeService;
	}

	@Transactional
	public CreateBottleMessageResponse createBottleMessage(Long userId, CreateBottleMessageRequest request) {
		SentimentAnalysisDto analyze = analysisMessage(createAIPrompt(request.content()));
		return CreateBottleMessageResponse.of(bottleMessageRepository.save(BottleMessageEntity.builder()
			.content(request.content())
			.user(userService.findUserById(userId))
			.title(request.title())
			.postcardUrl(request.postCard())
			.negativity(Negativity.valueOf(analyze.negativeImpact()))
			.build()).getId(), analyze);
	}

	private String createAIPrompt(String content) {
		return String.format(AnalyzeMessagePrompt.ANALYZE_MESSAGE_PROMPT, content);
	}

	private SentimentAnalysisDto analysisMessage(String content) {
		String response = claudeService.callClaudeForSentimentAnalysis(content);
		return parseSentimentString(response);
	}

	public SentimentAnalysisDto parseSentimentString(String input) {
		List<EmotionScore> emotions = new ArrayList<>();

		String[] lines = input.split("\n");

		for (int i = 2; i < 5; i++) {
			String line = lines[i].trim();
			if (line.contains("|")) {
				String[] columns = line.split("\\|");
				if (columns.length >= 3) {
					EmotionScore score = new EmotionScore(columns[1].trim(), Integer.parseInt(columns[2].trim()));
					emotions.add(score);
				}
			}
		}

		String negativeImpactLine = lines[lines.length - 1].replace("|", "").trim();
		return new SentimentAnalysisDto(emotions, negativeImpactLine);
	}

	@Transactional
	public BottleMessageDetailResponse getRandomBottleMessage(Long userId) {
		BottleMessageEntity message = findRandomUnreceivedMessage(userId);
		createBottleMessageReceiptLog(userId, message);
		return getDetailBottleMessage(message.getId(), userId);
	}

	private BottleMessageEntity findRandomUnreceivedMessage(Long userId) {
		List<BottleMessageEntity> unreceivedMessages = bottleMessageRepository.findUnreceivedMessagesByUserId(userId);
		return unreceivedMessages.get(0);
	}

	private void createBottleMessageReceiptLog(Long userId, BottleMessageEntity message) {
		bottleMessageLogRepository.save(
			BottleMessageReceiptLog.builder().recipient(userService.findUserById(userId)).message(message).build());
	}

	@Transactional(readOnly = true)
	public ReceivedBottleMessagesResponse getReceivedBottleMessages(Long userId) {
		List<BottleMessageEntity> savedMessages = bottleMessageRepository.findSavedMessagesByUserId(userId);
		return getReceivedMessageResponse(savedMessages, userId);
	}

	@Transactional
	public ReceivedBottleMessagesResponse deleteBottleMessage(Long userId, Long messageId) {
		Optional<BottleMessageReceiptLog> message = bottleMessageLogRepository.findByMessageIdAndRecipientId(
			messageId, userId);

		if (message.isPresent()) {
			message.get().delete();
		}

		return getReceivedBottleMessages(userId);
	}

	private ReceivedBottleMessagesResponse getReceivedMessageResponse(List<BottleMessageEntity> bottleMessageEntities,
		Long userId) {
		return new ReceivedBottleMessagesResponse(bottleMessageEntities.stream()
			.map(savedMessage -> {
					BottleMessageLogInfo log = getDateLog(savedMessage.getId(), userId);
					BottleMessageSummaryInfo messageInfo = savedMessage.toSummaryInfo();
					return new BottleMessageWithDateLogInfo(log, messageInfo);
				}
			)
			.toList());
	}

	private BottleMessageLogInfo getDateLog(Long messageId, Long userId) {
		BottleMessageReceiptLog log = bottleMessageLogRepository.findByMessageIdAndRecipientId(messageId,
			userId).get();
		BottleMessageEntity message = getBottleMessageEntity(messageId);
		return new BottleMessageLogInfo(localDateToString(log.getReceivedAt(), "yyyy-MM-dd HH:mm:ss"),
			localDateToString(message.getCreatedAt(), "yyyy-MM-dd HH:mm:ss"));
	}

	@Transactional(readOnly = true)
	public BottleMessageDetailResponse getDetailBottleMessage(Long messageId, Long userId) {
		BottleMessageEntity message = getBottleMessageEntity(messageId);
		BottleMessageReactionResponse reaction = getReactionCount(messageId, isReacted(userId, messageId));
		return new BottleMessageDetailResponse(message.toDetailInfo(), reaction);
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

	private String localDateToString(LocalDateTime date, String format) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
		return date.format(formatter);
	}

	@Transactional(readOnly = true)
	public SentBottleMessageResponse getSentBottleMessages(Long userId) {
		List<BottleMessageEntity> sentMessages = bottleMessageRepository.findBySenderId(userId);
		return getSentMessagesResponse(sentMessages);
	}

	private SentBottleMessageResponse getSentMessagesResponse(List<BottleMessageEntity> bottleMessageEntities) {
		return new SentBottleMessageResponse(bottleMessageEntities.stream()
			.map(entity ->
				new SentBottleMessageInfo(localDateToString(entity.getCreatedAt(), "yyyy-MM-dd HH:mm:ss"),
					entity.toSummaryInfo())
			)
			.toList());
	}

	@Transactional
	public void deleteBottleMessageReaction(Long messageId, Long userId, String reactionType) {
		bottleMessageReactionRepository.deleteByMessageIdAndReactorAndReactionType(messageId,
			userService.findUserById(userId), ReactionType.valueOf(reactionType));
	}

	private BottleMessageEntity getBottleMessageEntity(Long messageId) {
		return bottleMessageRepository.findById(messageId)
			.orElseThrow(() -> new NoSuchElementException("Message with ID " + messageId + " not found."));
	}

	@Transactional(readOnly = true)
	public RecentSentAtResponse getRecentSendTime(Long userId) {
		Optional<BottleMessageEntity> recentMessage = bottleMessageRepository.findTopBySenderIdOrderByCreatedAtDesc(
			userId);
		if (recentMessage.isPresent()) {
			String recentSentAt = localDateToString(recentMessage.get().getCreatedAt(), "yyyy-MM-dd HH:mm:ss");
			return new RecentSentAtResponse(recentSentAt);
		} else {
			return new RecentSentAtResponse("작성한 글이 없습니다.");
		}
	}
}
