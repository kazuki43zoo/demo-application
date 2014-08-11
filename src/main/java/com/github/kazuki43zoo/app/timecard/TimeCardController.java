package com.github.kazuki43zoo.app.timecard;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.github.kazuki43zoo.domain.service.security.CustomUserDetails;
import com.github.kazuki43zoo.web.security.CurrentUser;

@RequestMapping("timecards")
@Controller
public class TimeCardController {

    @RequestMapping(method = RequestMethod.GET)
    public String viewTimeCard(@CurrentUser CustomUserDetails currentUser, Model model) {
        model.addAttribute(currentUser.getAccount());
        return "timecard/form";
    }

}
