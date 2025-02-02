package com.example.bloombackend.quest.service.prompt;

import com.example.bloombackend.quest.entity.QuestEntity;
import com.example.bloombackend.quest.entity.UserQuestLogEntity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class QuestRecommendationPrompt {

    private final ObjectMapper objectMapper;

    private static final String QUEST_RECOMMEND_PROMPT = """
        다음은 사용자에게 제공할 수 있는 전체 퀘스트 목록과,
        각 퀘스트의 완료 여부/선택 일자 등 유저 퀘스트 로그 정보입니다.

        1. questList (전체 퀘스트 목록)
        %s

        2. userQuestLogs (유저가 완료했거나 진행 중인 퀘스트 기록)
        %s

        위 두 정보를 종합 분석해,
        “사용자에게 가장 적합한 퀘스트 3개”를 골라서 추천해주세요.

        - 추천 시 고려할 요소:
          - 사용자가 최근 완료한 퀘스트는 후순위, 하지만 꾸준히 선택해왔던 퀘스트라면 선순위
          - 이미 진행 중인(선택했지만 아직 끝내지 않은) 퀘스트는 제외
          - 이전에 시도한 적 없거나, 중간에 포기했다면 다시 추천할 수도 있음
          - 사용자의 난이도 취향, 최근 활동 빈도 등의 추가 정보를 고려할 수도 있음

        최종 결과는 오직 JSON 형식으로만 반환해주세요.

        형식 예시:
        {
          "recommendedQuestIds": [1, 2, 3]
        }

        JSON 이외의 문장은 절대 포함하지 마세요.
        """;

    public QuestRecommendationPrompt(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public String createPrompt(List<QuestEntity> quests, List<UserQuestLogEntity> userQuestLogs) throws JsonProcessingException {
        String questListJson = convertQuestsToJson(quests);
        String userQuestLogsJson = convertUserQuestLogsToJson(userQuestLogs);
        return String.format(QUEST_RECOMMEND_PROMPT, questListJson, userQuestLogsJson);
    }

    private String convertQuestsToJson(List<QuestEntity> questList) throws JsonProcessingException {
        List<QuestForPrompt> questForPromptData = questList.stream()
                .map(QuestEntity::toPromptData)
                .toList();
        Map<String, Object> questListJson = Map.of("quests", questForPromptData);
        return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(questListJson);
    }

    private String convertUserQuestLogsToJson(List<UserQuestLogEntity> userQuestLogs) throws JsonProcessingException {
        List<UserQuestLogForPrompt> userQuestLogForPromptData = userQuestLogs.stream()
                .map(UserQuestLogEntity::toPromptData)
                .toList();
        Map<String, Object> userQuestLogsJson = Map.of("userQuestLogs", userQuestLogForPromptData);
        return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(userQuestLogsJson);
    }
}