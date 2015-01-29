package com.github.kazuki43zoo.app.welcome;

import com.github.kazuki43zoo.app.auth.LoginFormControllerAdvice;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.terasoluna.gfw.web.token.transaction.TransactionTokenCheck;
import org.terasoluna.gfw.web.token.transaction.TransactionTokenType;

@Controller
@LoginFormControllerAdvice.LoginFormModelAttribute
public class HomeController {

    @TransactionTokenCheck(type = TransactionTokenType.BEGIN)
    @RequestMapping(value = "/")
    public String home() {
        return "welcome/home";
    }

}
