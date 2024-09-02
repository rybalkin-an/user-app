package com.github.rybalkin_an.app.user.service.impl;

import com.github.rybalkin_an.app.user.client.ExternalApiClient;
import com.github.rybalkin_an.app.user.exception.BusinessException;
import com.github.rybalkin_an.app.user.exception.NotFoundException;
import com.github.rybalkin_an.app.user.model.User;
import com.github.rybalkin_an.app.user.model.UserData;
import com.github.rybalkin_an.app.user.repository.UserRepository;
import com.github.rybalkin_an.app.user.service.UserService;
import com.github.rybalkin_an.app.utils.StringHelper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    private final ExternalApiClient externalApiClient;

    @Autowired
    public UserServiceImpl(
            UserRepository userRepository,
            WebClient.Builder webClientBuilder,
            @Value("${external.api.url}") String externalApiUrl) {
        this.userRepository = userRepository;
        WebClient webClient = webClientBuilder.baseUrl(externalApiUrl).build();
        HttpServiceProxyFactory httpServiceProxyFactory = HttpServiceProxyFactory
                .builder(WebClientAdapter.forClient(webClient))
                .build();
        this.externalApiClient = httpServiceProxyFactory.createClient(ExternalApiClient.class);
    }


    @Transactional(readOnly = true)
    public List<User> findAll() {
        return this.userRepository.findAll();
    }

    @Transactional(readOnly = true)
    public User findById(UUID id) {
        return this.userRepository.findById(id).orElseThrow(NotFoundException::new);
    }

    public User create(@Valid User userToCreate) {
        userToCreate.setRegistrationDate(StringHelper.getCurrentDate());
        userToCreate.setVersion(1);
        return this.userRepository.save(userToCreate);
    }

    public User update(UUID id, @Valid User userToUpdate) {
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
        try {
            UserData userData = externalApiClient.getUserData();
            if (userData.getUserId() == null ||
                    userData.getTitle() == null ||
                    userData.getId() == null ||
                    userData.getCompleted() == null) {
                throw new BusinessException("User data is not exist");
            }
            dbUser.setUserData(userData);
            return this.userRepository.save(dbUser);
        } catch (WebClientResponseException e) {
            throw new BusinessException(e.getMessage());
        }
    }
}