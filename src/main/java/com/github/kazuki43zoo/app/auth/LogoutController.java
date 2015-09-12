package com.github.kazuki43zoo.app.auth;

import com.github.kazuki43zoo.core.message.Message;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RequestMapping("auth")
@Controller
public class LogoutController {

    @RequestMapping(path = "logout", method = RequestMethod.GET, params = "success")
    public String logoutSuccess(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute(Message.AUTH_LOGOUT.resultMessages());
        return "redirect:/";
    }

}
