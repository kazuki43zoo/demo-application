package com.github.kazuki43zoo.app.error;

import com.github.kazuki43zoo.app.auth.LoginForm;
import com.github.kazuki43zoo.core.message.Message;
import org.springframework.security.web.WebAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;

@RequestMapping("error")
@Controller
public class ErrorController {

    @RequestMapping("invalidSession")
    public String handleSessionError(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute(
                Message.FW_VALID_SESSION_NOT_EXISTS.resultMessages());
        return "redirect:/";
    }

    @RequestMapping(path = "login", method = RequestMethod.POST)
    public String handleLoginError(
            LoginForm form,
            RedirectAttributes redirectAttributes,
            HttpServletRequest request) {
        redirectAttributes.addFlashAttribute(form);
        redirectAttributes.addFlashAttribute(
                WebAttributes.AUTHENTICATION_EXCEPTION,
                request.getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION));
        return "redirect:/auth/login";
    }

}
