package com.github.kazuki43zoo.app.welcome;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.github.kazuki43zoo.auth.login.LoginForm;

@Controller
public class HomeController {

    @ModelAttribute
    public LoginForm setUpLoginForm() {
        return new LoginForm();
    }

    @RequestMapping(value = "/")
    public String home() {
        return "welcome/home";
    }

}
