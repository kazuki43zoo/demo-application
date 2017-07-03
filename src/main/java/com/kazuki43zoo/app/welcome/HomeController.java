package com.kazuki43zoo.app.welcome;

import com.kazuki43zoo.app.auth.LoginFormControllerAdvice;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.terasoluna.gfw.web.token.transaction.TransactionTokenCheck;
import org.terasoluna.gfw.web.token.transaction.TransactionTokenType;

@Controller
@LoginFormControllerAdvice.LoginFormModelAttribute
public class HomeController {

    @TransactionTokenCheck(type = TransactionTokenType.BEGIN)
    @GetMapping("/")
    public String home() {
        return "welcome/home";
    }

}
