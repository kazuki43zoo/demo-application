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

import static com.github.kazuki43zoo.app.auth.LoginFormControllerAdvice.LoginFormModelAttribute;

@RequestMapping("auth")
@Controller
@LoginFormModelAttribute
public class LoginController {

    @Inject
    LoginSharedHelper loginSharedHelper;

    @TransactionTokenCheck(type = TransactionTokenType.BEGIN)
    @RequestMapping(path = "login", method = RequestMethod.GET)
    public String showLoginForm() {
        return "auth/loginForm";
    }

    @RequestMapping(path = "login", method = RequestMethod.GET, params = "encourage")
    public String encourageLogin(final RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute(Message.AUTH_ENCOURAGE_LOGIN.resultMessages());
        return "redirect:/app/auth/login";
    }

    @TransactionTokenCheck
    @RequestMapping(path = "login", method = RequestMethod.POST)
    public String login(final @Validated LoginForm form, final BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return showLoginForm();
        }
        return loginSharedHelper.generateAuthenticationProcessingUrl(form.getUsername());
    }


    @RequestMapping(path = "error", method = RequestMethod.POST)
    public String handleLoginError(final LoginForm form, final RedirectAttributes redirectAttributes, final HttpServletRequest request) {
        redirectAttributes.addFlashAttribute(form);
        redirectAttributes.addFlashAttribute(WebAttributes.AUTHENTICATION_EXCEPTION, request.getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION));
        return "redirect:/app/auth/login";
    }

}
