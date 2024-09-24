package com.example.bloombackend.bottlemsg.sevice;

public class AnalyzeMessagePrompt {
	public static final String ANALYZE_MESSAGE_PROMPT = "%s"
		+ "의 감정분석을 하고 타인에게 부정적 영향을 끼치는 여부를 판단해줘."
		+ "감정이 조금 부정적이라도 공감을 살 수 있거나 수위가 낮으면 부정적 영향은 낮게 평가해줘."
		+ "심한 욕설이 포함 된 경우는 부정적 영향을 높게 평가해줘"
		+ "응답 포멧은 [관련된 감정 3가지][퍼센트],[부정적영향여부(UPPER, MIDDLE, LOWER)] 를 다른 말은 포함하지말고"
		+ "| 관련된 감정 | 퍼센트 |\n"
		+ "|-------------|--------|\n"
		+ "| 우울함      | 70    |\n"
		+ "| 외로움      | 60    |\n"
		+ "| 부정적 영향여부 |\n"
		+ "| ------------ |\n"
		+ "|	 UPEER	  | 이 예시 포멧의 표만 보여줘";

}
