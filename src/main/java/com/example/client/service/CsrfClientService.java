
package com.example.client.service;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.annotation.PostConstruct;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class CsrfClientService {

    private final WebClient baseClient = WebClient.builder()
            .baseUrl("http://localhost:8080")
            .build();

    private String csrfToken;
    private String jsessionId;

    public Mono<Void> authenticateAndFetchCsrf() {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("username", "admin");
        formData.add("password", "admin");

        return baseClient.get()
                .uri("/api/csrf")
                .retrieve()
                .bodyToMono(JsonNode.class)
                .map(json -> json.get("token").asText())
                .doOnNext(token -> csrfToken = token)
                .flatMap(csrfToken -> {
                    return baseClient.post()
                            .uri("/api/authentication")
                            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                            .header("X-XSRF-TOKEN", csrfToken)
                            .header(HttpHeaders.COOKIE, "XSRF-TOKEN=" + csrfToken)
                            .bodyValue(formData)
                            .exchangeToMono(response -> {
                                String setCookie = String.join(";", response.headers().asHttpHeaders().get(HttpHeaders.SET_COOKIE));
                                jsessionId = extractCookie(setCookie, "SESSION");
                                return Mono.empty();
                            });
                }).then();
    }

    public Mono<Void> logout() {
        return baseClient.post()
                .uri("/api/logout")
                .header("X-XSRF-TOKEN", csrfToken)
                .header(HttpHeaders.COOKIE, "SESSION=" + jsessionId + "; XSRF-TOKEN=" + csrfToken)
                .exchangeToMono(response -> {
                    int statusCode = response.statusCode().value();
                    System.err.println("statusCode = " + statusCode);
                    return Mono.empty();
                });
    }

    public Mono<AdminUserDTO> callProtectedEndpoint() {
        return baseClient.get()
                .uri("/api/account")
//                        .header("X-XSRF-TOKEN", csrfToken)
                .header(HttpHeaders.COOKIE, "SESSION=" + jsessionId + "; XSRF-TOKEN=" + csrfToken)
                .retrieve()
                .bodyToMono(AdminUserDTO.class);
    }

    public Flux<ConsumerDTO> callProtectedEndpoint2() {
        return baseClient.get()
                .uri("/services/administration/api/consumers?page=0&size=20&sort=id,asc")
                .header(HttpHeaders.COOKIE, "SESSION=" + jsessionId + "; XSRF-TOKEN=" + csrfToken)
                .retrieve()
                .bodyToFlux(ConsumerDTO.class);
    }

    private String extractCookie(String cookieHeader, String name) {
        Pattern pattern = Pattern.compile(name + "=([^;]+)");
        Matcher matcher = pattern.matcher(cookieHeader);
        return matcher.find() ? matcher.group(1) : null;
    }
}
