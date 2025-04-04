
package com.example.client.controller;

import com.example.client.service.AdminUserDTO;
import com.example.client.service.ConsumerDTO;
import com.example.client.service.CsrfClientService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class TestController {

    private final CsrfClientService csrfClientService;

    public TestController(CsrfClientService csrfClientService) {
        this.csrfClientService = csrfClientService;
    }

    @GetMapping("/test-call")
    public Mono<AdminUserDTO> callProtectedApi() {
        return csrfClientService.callProtectedEndpoint();
    }

    @GetMapping("/test-call2")
    public Flux<ConsumerDTO> callProtectedApi2() {
        return csrfClientService.callProtectedEndpoint2();
    }

    @GetMapping("/login")
    public Mono<Void> login() {
        return csrfClientService.authenticateAndFetchCsrf();
    }

    @GetMapping("/logout")
    public Mono<Void> logout() {
        return csrfClientService.logout();
    }
}

