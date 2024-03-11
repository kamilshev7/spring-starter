package guard.passer.core.http.controller;

import guard.passer.core.dto.LoginDto;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LoginController {

    @GetMapping("/login")// <---                    <---
    public String loginPage(){                                 // ^
        return "user/login";                                   // |
    }

//    @PostMapping("/login")                                  // |
//    public String login(@ModelAttribute() LoginDto loginDto){  // |
//        return "redirect:/login"; // перекинет на GET /login --->
//    }
}
