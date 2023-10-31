package com.github.rybalkin_an.app.user.client;

import com.github.rybalkin_an.app.user.model.UserData;
import com.maciejwalkowiak.spring.http.annotation.HttpClient;
import org.springframework.web.service.annotation.GetExchange;

@HttpClient("todos")
public interface ExternalApiClient {

    @GetExchange("/1")
    UserData getUserData();
}
