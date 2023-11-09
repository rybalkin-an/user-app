package com.github.rybalkin_an.app.user.client;

import com.github.rybalkin_an.app.user.model.UserData;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange
public interface ExternalApiClient {

    @GetExchange("/todos/1")
    UserData getUserData();
}
