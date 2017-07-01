package com.github.kazuki43zoo.app.welcome;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.terasoluna.gfw.web.token.transaction.TransactionTokenCheck;
import org.terasoluna.gfw.web.token.transaction.TransactionTokenType;

import static com.github.kazuki43zoo.app.auth.LoginFormControllerAdvice.LoginFormModelAttribute;

@Controller
@LoginFormModelAttribute
public class HomeController {

    @TransactionTokenCheck(type = TransactionTokenType.BEGIN)
    @GetMapping("/")
    public String home() {
        return "welcome/home";
    }

}
