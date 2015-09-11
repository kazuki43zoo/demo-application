package com.github.kazuki43zoo.app.auth;

import com.github.kazuki43zoo.core.message.Message;
import org.springframework.security.web.WebAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.terasoluna.gfw.web.token.transaction.TransactionTokenCheck;
import org.terasoluna.gfw.web.token.transaction.TransactionTokenType;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

@RequestMapping("auth/login")
@Controller
@LoginFormControllerAdvice.LoginFormModelAttribute
public class LoginController {

    @Inject
    LoginSharedHelper loginSharedHelper;

    @TransactionTokenCheck(type = TransactionTokenType.BEGIN)
    @RequestMapping(method = RequestMethod.GET)
    public String showLoginForm() {
        return "auth/loginForm";
    }

    @RequestMapping(method = RequestMethod.GET, params = "encourage")
    public String encourageLogin(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute(
                Message.AUTH_ENCOURAGE_LOGIN.resultMessages());
        return "redirect:/app/auth/login";
    }

    @TransactionTokenCheck
    @RequestMapping(method = RequestMethod.POST)
    public String login(
            @Validated LoginForm form,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return showLoginForm();
        }
        return loginSharedHelper.generateAuthenticationProcessingUrl(
                form.getAccountId());
    }

}
