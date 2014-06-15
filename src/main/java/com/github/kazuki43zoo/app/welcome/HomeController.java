package com.github.kazuki43zoo.app.welcome;

import java.sql.SQLException;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.terasoluna.gfw.web.token.transaction.TransactionTokenCheck;
import org.terasoluna.gfw.web.token.transaction.TransactionTokenType;

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
    @TransactionTokenCheck(type = TransactionTokenType.BEGIN)
    public String home(Locale locale, Model model) throws SQLException {
        logger.info("Welcome home! The client locale is {}.", locale);

        Date date = welcomeService.getCurrentDate();
        DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG,
                locale);

        String formattedDate = dateFormat.format(date);

        model.addAttribute("serverTime", formattedDate);

        return "welcome/home";
    }

}
