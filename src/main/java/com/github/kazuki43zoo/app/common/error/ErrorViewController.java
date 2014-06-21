package com.github.kazuki43zoo.app.common.error;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.github.kazuki43zoo.core.message.Messages;

@RequestMapping("error")
@Controller
public class ErrorViewController {

    @RequestMapping("invalidSession")
    public String handleSessionError(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute(Messages.FW_VALID_SESSION_NOT_EXISTS
                .buildResultMessages());
        return "redirect:/app/";
    }

}
