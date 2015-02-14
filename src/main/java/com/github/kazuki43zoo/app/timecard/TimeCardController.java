package com.github.kazuki43zoo.app.timecard;

import com.github.kazuki43zoo.domain.service.security.CustomUserDetails;
import com.github.kazuki43zoo.web.security.CurrentUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@RequestMapping("timecards")
@Controller
public class TimeCardController {

    @RequestMapping(method = RequestMethod.GET)
    public String viewTimeCard(
            @CurrentUser CustomUserDetails authenticatedUser,
            Model model) {
        model.addAttribute(authenticatedUser.getAccount());
        return "timecard/form";
    }

}
