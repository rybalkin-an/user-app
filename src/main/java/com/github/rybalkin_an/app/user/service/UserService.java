package com.github.rybalkin_an.app.user.service;

import com.github.rybalkin_an.app.user.model.User;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public interface UserService extends CrudService<UUID, User> {
}
