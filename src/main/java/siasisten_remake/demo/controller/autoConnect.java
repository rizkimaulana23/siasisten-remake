package siasisten_remake.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class autoConnect {

    @RequestMapping("/**")
    public String redirectToLogin() {
        return "redirect:/login";
    }
}
