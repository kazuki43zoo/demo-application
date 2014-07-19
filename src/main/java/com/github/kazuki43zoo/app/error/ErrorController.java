package com.github.kazuki43zoo.app.error;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.github.kazuki43zoo.core.message.Message;

@RequestMapping("error")
@Controller
public class ErrorController {

    @RequestMapping("invalidSession")
    public String handleSessionError(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute(Message.FW_VALID_SESSION_NOT_EXISTS
                .resultMessages());
        return "redirect:/";
    }

}
