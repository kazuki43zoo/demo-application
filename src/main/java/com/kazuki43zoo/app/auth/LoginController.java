package com.kazuki43zoo.app.auth;

import com.kazuki43zoo.core.message.Message;
import org.springframework.security.web.WebAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.terasoluna.gfw.web.token.transaction.TransactionTokenCheck;
import org.terasoluna.gfw.web.token.transaction.TransactionTokenType;

import javax.servlet.http.HttpServletRequest;

@RequestMapping("auth")
@Controller
@LoginFormControllerAdvice.LoginFormModelAttribute
@lombok.RequiredArgsConstructor
public class LoginController {

    private final LoginSharedHelper loginSharedHelper;

    @TransactionTokenCheck(type = TransactionTokenType.BEGIN)
    @GetMapping(path = "login")
    public String showLoginForm() {
        return "auth/loginForm";
    }

    @GetMapping(path = "login", params = "encourage")
    public String encourageLogin(final RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute(Message.AUTH_ENCOURAGE_LOGIN.resultMessages());
        return "redirect:/app/auth/login";
    }

    @TransactionTokenCheck
    @PostMapping(path = "login")
    public String login(final @Validated LoginForm form, final BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return showLoginForm();
        }
        return this.loginSharedHelper.generateAuthenticationProcessingUrl(form.getUsername());
    }


    @PostMapping(path = "error")
    public String handleLoginError(final LoginForm form, final RedirectAttributes redirectAttributes, final HttpServletRequest request) {
        redirectAttributes.addFlashAttribute(form);
        redirectAttributes.addFlashAttribute(WebAttributes.AUTHENTICATION_EXCEPTION, request.getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION));
        return "redirect:/app/auth/login";
    }

}
