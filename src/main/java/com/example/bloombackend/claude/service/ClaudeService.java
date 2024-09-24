package com.example.bloombackend.claude.service;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ClaudeService {

	@Value("${claude.api.key}")
	private String apiKey;

	@Value("${claude.api.model}")
	private String modelName;

	@Value("${claude.api.anthropic-version}")
	private String anthropicVersion;

	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	private ClaudeResponseHandler claudeResponseHandler;

	public String callClaudeForSentimentAnalysis(String prompt) {
		String apiUrl = "https://api.anthropic.com/v1/messages";

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("x-api-key", apiKey);
		headers.set("anthropic-version", anthropicVersion);

		JSONObject requestBody = new JSONObject();
		requestBody.put("model", modelName);
		requestBody.put("max_tokens", 1024);

		JSONObject message = new JSONObject();
		message.put("role", "user");
		message.put("content", prompt);

		requestBody.put("messages", new JSONArray().put(message));

		HttpEntity<String> entity = new HttpEntity<>(requestBody.toString(), headers);

		String response = restTemplate.exchange(
			apiUrl,
			HttpMethod.POST,
			entity,
			String.class
		).getBody();

		System.out.println(claudeResponseHandler.processClaudeResponse(response));

		return claudeResponseHandler.processClaudeResponse(response);
	}
}
