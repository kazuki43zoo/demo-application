package com.kazuki43zoo.app.timecard;

import com.kazuki43zoo.core.security.CurrentUser;
import com.kazuki43zoo.domain.service.security.CustomUserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("timecards")
@Controller
public class TimeCardController {

    @GetMapping
    public String viewTimeCard(final @CurrentUser CustomUserDetails authenticatedUser, final Model model) {
        model.addAttribute(authenticatedUser.getAccount());
        return "timecard/form";
    }

}
