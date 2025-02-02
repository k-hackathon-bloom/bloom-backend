package com.example.bloombackend.global;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.AccessToken;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.FileInputStream;
import java.io.IOException;

@Component
public class FcmUtil {

    private static final String FCM_API_URL = "https://fcm.googleapis.com/v1/projects/bloom-7d852/messages:send";
    private static final String SERVICE_ACCOUNT_FILE = "src/main/resources/bloom-7d852-firebase-adminsdk-7tm8o-ec53203970.json";

    private String getFcmAccessToken() throws IOException {
        GoogleCredentials credentials = GoogleCredentials
            .fromStream(new FileInputStream(SERVICE_ACCOUNT_FILE))
            .createScoped("https://www.googleapis.com/auth/firebase.messaging");

        credentials.refreshIfExpired();
        AccessToken token = credentials.getAccessToken();
        return token.getTokenValue();
    }

    public void sendNotification(String targetToken, String title, String body) throws IOException {
        String accessToken = getFcmAccessToken();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(accessToken);

        String payload = "{"
                + "\"message\":{"
                + "\"fcmToken\":\"" + targetToken + "\","
                + "\"notification\":{"
                + "\"title\":\"" + title + "\","
                + "\"body\":\"" + body + "\""
                + "}"
                + "}"
                + "}";

        HttpEntity<String> request = new HttpEntity<>(payload, headers);

        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.postForObject(FCM_API_URL, request, String.class);
    }
}