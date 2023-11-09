package com.github.rybalkin_an.app.user.model;


import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserData {

    private Long userId;

    @Id
    private Long id;

    private String title;

    private Boolean completed;
}