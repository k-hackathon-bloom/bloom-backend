package com.example.bloombackend.bottlemsg.service;

public class AnalyzeMessagePrompt {
	public static final String ANALYZE_MESSAGE_PROMPT = "%s\n\n" +

			"너는 감정 분석 전문가야. 아래 규칙을 반드시 지켜서만 응답해야 해.\n\n" +

			"🎯 분석 대상 글에 대해 감정 분석을 수행하고, 부정적 영향의 수준을 평가해줘.\n\n" +

			"📌 분석 규칙:\n" +
			"1. 감정이 조금 부정적이라도 공감을 살 수 있거나 수위가 낮으면 부정적 영향은 낮게 평가해줘.\n" +
			"2. 심한 욕설이 포함되면 부정적 영향은 높게 평가해줘.\n\n" +
			"3. 의미 없는 내용은 `UNKNOWN`로 판단해.**\n\n"+

			"📌 응답 포맷: 아래의 Markdown 표 2개만 출력해. 절대로 표 외에 다른 텍스트를 포함하지 마.\n\n" +

			"```\n" +
			"| 관련된 감정 | 퍼센트 |\n" +
			"|-------------|--------|\n" +
			"| (감정1)     | (숫자1) |\n" +
			"| (감정2)     | (숫자2) |\n" +
			"| (감정3)     | (숫자3) |\n" +
			"| 부정적 영향여부 |\n" +
			"|----------------|\n" +
			"| UPPER / MIDDLE / LOWER / UNKNOWN |\n" +
			"```\n\n" +

			"✅ 반드시 위 표의 구조만 유지해서 답변해.\n" +
			"❌ 표 외의 안내문, 문장, 설명, 서두/결론 모두 금지.\n" +
			"❌ 분석 실패도 절대 금지. 입력이 짧더라도 표 형식에 맞게 결과를 출력해.\n";

	public static String createAIPrompt(String content) {
		return String.format(ANALYZE_MESSAGE_PROMPT, content);
	}

}
