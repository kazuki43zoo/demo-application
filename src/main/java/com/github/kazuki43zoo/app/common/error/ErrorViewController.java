package com.github.kazuki43zoo.app.common.error;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.terasoluna.gfw.common.message.ResultMessages;

@RequestMapping("error")
@Controller
public class ErrorViewController {

    @RequestMapping("invalidSession")
    public String handleSessionError(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute(ResultMessages.danger().add("e.xx.fw.5002"));
        return "redirect:/";
    }

}
