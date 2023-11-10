package com.github.rybalkin_an.app.user.controller;

import com.github.rybalkin_an.app.user.model.User;
import com.github.rybalkin_an.app.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/users")
public class UserViewController {

    @Autowired
    private UserService userService;

    @GetMapping("/manage")
    public String showManageUsers(Model model) {
        model.addAttribute("user", new User());
        List<User> userList = userService.findAll();
        model.addAttribute("users", userList);
        return "userManagement";
    }

    @PostMapping("/createUser")
    public String createUser(@Valid @ModelAttribute("user") User user, BindingResult result) {
        if (result.hasErrors()) {
            return "userManagement";
        }
        userService.create(user);
        return "redirect:/users/manage";
    }
    @PostMapping("/deleteUser")
    public String deleteUser(@RequestParam UUID userId) {
        userService.delete(userId);
        return "redirect:/users/manage";
    }
}

