package com.github.kazuki43zoo.auth.logout;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.github.kazuki43zoo.core.message.Messages;

@Controller
public class LogoutController {

    @RequestMapping(value = "logoutSuccess", method = RequestMethod.GET)
    public String logoutSuccess(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute(Messages.AUTH_LOGOUT.buildResultMessages());
        return "redirect:/";
    }

}
