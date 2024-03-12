package guard.passer.core.http.controller;

import guard.passer.core.database.entity.UserRole;
import guard.passer.core.dto.UserReadDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping(value = "/api")
@SessionAttributes("user") // чтобы user хранился в сессии, без этого только в запросе
public class GreetingController {

    @ModelAttribute("roles")
    public List<UserRole> roles(){
        return Arrays.asList(UserRole.values());
    }

    // http://localhost:8080/api/hello3?id=2&username=kamil
    @GetMapping(value = "/hello3")
    public String hello3(Model model, @ModelAttribute("userReadDto") UserReadDto userReadDto) {
        model.addAttribute("user", userReadDto);
        return "greeting/hello";
    }

    // http://localhost:8080/api/hello
    @GetMapping(value = "/hello")
    public String hello1(Model model, HttpServletRequest request) {

//        request.setAttribute("user"); // requestScope
//        request.getSession().setAttribute("user"); // sessionScope
        model.addAttribute("user", new UserReadDto(1L, "Ivan",
                null, null, null, null, null, null, null));
        return "greeting/hello";
    }

    // http://localhost:8080/api/hello/19?age=5
    @GetMapping(value = "/hello/{id}")
    public String hello2(@RequestParam Integer age, // можно не задавать имя,
                         @RequestHeader String accept, // если оно совпадает
                         @CookieValue("JSESSIONID") String cookie,
                         @PathVariable("id") Integer id) {
        return "greeting/hello";
    }

    @GetMapping(value = "/bye")
    public String bye(@SessionAttribute("user") UserReadDto user) {
        return "greeting/bye";
    }
}
