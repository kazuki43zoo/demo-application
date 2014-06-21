package com.github.kazuki43zoo.auth.login;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.web.WebAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class LoginController {
    @ModelAttribute
    public LoginForm setUpLoginForm() {
        return new LoginForm();
    }

    @RequestMapping(value = "login", method = RequestMethod.GET)
    public String viewLoginForm() {
        return "auth/loginForm";
    }

    @RequestMapping(value = "login", method = RequestMethod.POST)
    public String login(@Validated LoginForm form, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return viewLoginForm();
        }
        return "forward:/auth/authenticate";
    }

    @RequestMapping(value = "error", method = RequestMethod.POST)
    public String handleLoginError(LoginForm form, RedirectAttributes redirectAttributes,
            HttpServletRequest request) {
        redirectAttributes.addFlashAttribute(form);
        redirectAttributes.addFlashAttribute(WebAttributes.AUTHENTICATION_EXCEPTION,
                request.getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION));
        return "redirect:/auth/login";
    }

}
