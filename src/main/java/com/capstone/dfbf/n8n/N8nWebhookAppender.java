package com.capstone.dfbf.n8n;

import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.classic.spi.StackTraceElementProxy;
import ch.qos.logback.core.AppenderBase;
import ch.qos.logback.classic.spi.ILoggingEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Setter;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Setter
public class N8nWebhookAppender extends AppenderBase<ILoggingEvent> {

    private String webhookUrl;
    private String serviceName;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_1_1) // HTTP 1.1 강제 사용 (호환성 향상)
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    @Override
    protected void append(ILoggingEvent eventObject) {
        if (!isStarted()) return;
        if (!eventObject.getLevel().toString().equals("ERROR")) return;

        System.out.println("[N8nWebhookAppender] Processing ERROR log: " + eventObject.getFormattedMessage());

        try {
            Map<String, Object> logData = new HashMap<>();
            logData.put("service_name", serviceName);
            logData.put("timestamp", eventObject.getTimeStamp());
            logData.put("level", eventObject.getLevel().toString());
            logData.put("message", eventObject.getFormattedMessage());
            logData.put("logger", eventObject.getLoggerName());

            if (eventObject.getThrowableProxy() != null) {
                logData.put("stack_trace", getStackTrace(eventObject.getThrowableProxy()));
            } else {
                logData.put("stack_trace", "No stack trace available");
            }

            sendToN8n(logData);
        } catch (Exception e) {
            System.err.println("[N8nWebhookAppender] Failed to process log: " + e.getMessage());
            e.printStackTrace();
            addError("Failed to send log to n8n: " + e.getMessage());
        }
    }

    private void sendToN8n(Map<String, Object> logData) {
        try {
            String jsonPayload = objectMapper.writeValueAsString(logData);
            System.out.println("[N8nWebhookAppender] Sending payload to " + webhookUrl);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(webhookUrl))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                    .timeout(Duration.ofSeconds(10))
                    .build();

            httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenAccept(response -> {
                        System.out.println("[N8nWebhookAppender] Response status: " + response.statusCode());
                        if (response.statusCode() != 200) {
                            System.err.println("[N8nWebhookAppender] Error response body: " + response.body());
                            addError("n8n responded with error: " + response.statusCode());
                        }
                    })
                    .exceptionally(e -> {
                        System.err.println("[N8nWebhookAppender] Async request failed: " + e.getMessage());
                        // e.printStackTrace(); // 로그가 너무 길어질 수 있어 주석 처리
                        return null;
                    });
        } catch (Exception e) {
            System.err.println("[N8nWebhookAppender] Error constructing request: " + e.getMessage());
            e.printStackTrace();
            addError("Error constructing n8n request: " + e.getMessage());
        }
    }

    private String getStackTrace(IThrowableProxy throwableProxy) {
        StringBuilder sb = new StringBuilder();
        sb.append(throwableProxy.getClassName()).append(": ").append(throwableProxy.getMessage()).append("\n");

        for (StackTraceElementProxy element : throwableProxy.getStackTraceElementProxyArray()) {
            sb.append("\t").append(element.toString()).append("\n");
        }

        return sb.toString();
    }
}
