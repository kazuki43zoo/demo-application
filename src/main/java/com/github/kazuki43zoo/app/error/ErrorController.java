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
    public String handleSessionError(final RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute(Message.FW_VALID_SESSION_NOT_EXISTS.resultMessages());
        return "redirect:/";
    }

}
