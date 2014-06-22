package com.github.kazuki43zoo.app.welcome;

import javax.inject.Inject;

import org.joda.time.DateTime;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.terasoluna.gfw.web.token.transaction.TransactionTokenCheck;

import com.github.kazuki43zoo.auth.login.LoginForm;
import com.github.kazuki43zoo.domain.service.welcome.WelcomeService;

@TransactionTokenCheck
@Controller
public class HomeController {

    @Inject
    WelcomeService welcomeService;

    @ModelAttribute
    public LoginForm setUpLoginForm() {
        return new LoginForm();
    }

    @ModelAttribute("serverTime")
    public DateTime setUpServerTime() {
        return welcomeService.getCurrentDateTime();
    }

    @RequestMapping(value = "/", method = { RequestMethod.GET, RequestMethod.POST })
    public String home() {
        return "welcome/home";
    }

}
