package com.kazuki43zoo.app.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("admin/h2Console")
@Controller
public class H2ConsoleController {
    @GetMapping
    public String showH2Console() {
        return "admin/h2Console";
    }
}
