package com.github.kazuki43zoo.app.welcome;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.terasoluna.gfw.web.token.transaction.TransactionTokenCheck;
import org.terasoluna.gfw.web.token.transaction.TransactionTokenType;

import com.github.kazuki43zoo.app.auth.LoginForm;

@Controller
public class HomeController {

    @ModelAttribute
    public LoginForm setUpLoginForm() {
        return new LoginForm();
    }

    @TransactionTokenCheck(type = TransactionTokenType.BEGIN)
    @RequestMapping(value = "/")
    public String home() {
        return "welcome/home";
    }

}
