package com.github.kazuki43zoo.app.welcome;

import java.sql.SQLException;
import java.util.Locale;

import javax.inject.Inject;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.github.kazuki43zoo.domain.service.welcome.WelcomeService;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {

    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

    @Inject
    WelcomeService welcomeService;

    /**
     * Simply selects the home view to render by returning its name.
     * 
     * @throws SQLException
     */
    @RequestMapping(value = "/", method = { RequestMethod.GET, RequestMethod.POST })
    public String home(Locale locale, Model model) throws SQLException {
        logger.info("Welcome home! The client locale is {}.", locale);

        DateTime dateTime = welcomeService.getCurrentDateTime();

        model.addAttribute("serverTime", dateTime);

        return "welcome/home";
    }

}
