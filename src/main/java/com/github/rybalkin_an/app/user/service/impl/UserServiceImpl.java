package com.github.rybalkin_an.app.user.service.impl;

import com.github.rybalkin_an.app.user.client.ExternalApiClient;
import com.github.rybalkin_an.app.user.exception.BusinessException;
import com.github.rybalkin_an.app.user.exception.NotFoundException;
import com.github.rybalkin_an.app.user.model.User;
import com.github.rybalkin_an.app.user.model.UserData;
import com.github.rybalkin_an.app.user.repository.UserRepository;
import com.github.rybalkin_an.app.user.service.UserService;
import com.github.rybalkin_an.app.utils.StringHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ExternalApiClient externalApiClient;

    @Transactional(readOnly = true)
    public List<User> findAll() {
        return this.userRepository.findAll();
    }

    @Transactional(readOnly = true)
    public User findById(UUID id) {
        return this.userRepository.findById(id).orElseThrow(NotFoundException::new);
    }

    public User create(User userToCreate) {
        userToCreate.setRegistrationDate(StringHelper.getCurrentDate());
        userToCreate.setVersion(1);
        return this.userRepository.save(userToCreate);
    }

    public User update(UUID id, User userToUpdate) {
        User dbUser = this.findById(id);
        if (!dbUser.getId().equals(userToUpdate.getId())) {
            throw new BusinessException("Update IDs must be the same.");
        }
        dbUser.setFirstName(userToUpdate.getFirstName());
        dbUser.setLastName(userToUpdate.getLastName());
        dbUser.setBirthdate(userToUpdate.getBirthdate());
        return this.userRepository.save(dbUser);
    }

    public void delete(UUID id) {
        User dbUser = this.findById(id);
        this.userRepository.delete(dbUser);
    }

    @Override
    public User saveUserDataToUser(UUID id) {
        User dbUser = this.findById(id);
        UserData userData = externalApiClient.getUserData();
        if (userData.getUserId() == null ||
                userData.getTitle() == null ||
                userData.getId() == null ||
                userData.getCompleted() == null) {
            throw new BusinessException("User data is not exist");
        }
        dbUser.setUserData(userData);
        return this.userRepository.save(dbUser);
    }
}