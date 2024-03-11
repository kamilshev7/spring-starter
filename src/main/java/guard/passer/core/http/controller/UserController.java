package guard.passer.core.http.controller;

import guard.passer.core.database.entity.UserRole;
import guard.passer.core.dto.PageResponse;
import guard.passer.core.dto.UserCreateEditDto;
import guard.passer.core.dto.UserFilter;
import guard.passer.core.dto.UserReadDto;
import guard.passer.core.service.CompanyService;
import guard.passer.core.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final CompanyService companyService;

    @GetMapping("/")
    public String findAll(Model model, UserFilter filter, Pageable pageable) {
        Page<UserReadDto> page = userService.findAll(filter, pageable);
        model.addAttribute("users", PageResponse.of(page));
        model.addAttribute("filter", filter);
        return "user/users";
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String findById(@PathVariable Long id, Model model) {
        return userService.findById(id)
                .map(user -> {
                    model.addAttribute("user", user);
                    model.addAttribute("roles", UserRole.values());
                    model.addAttribute("companies", companyService.findAll());
                    return "user/user";
                }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/registration")
    public String registration(Model model, @ModelAttribute("user") UserCreateEditDto user) {
        model.addAttribute("user", user);
        model.addAttribute("roles", UserRole.values());
        model.addAttribute("companies", companyService.findAll());
        return "user/registration";
    }

    @PostMapping("/")
//    @ResponseStatus(HttpStatus.CREATED)
    public String create(@ModelAttribute @Validated UserCreateEditDto user,
                         BindingResult bindingResult, // всегда ставится после валидируемого объекта
                         RedirectAttributes redirectAttributes) {
        if(bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("user", user);
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            return "redirect:/users/registration";
        }
        userService.create(user);
        return "redirect:/login";
    }

//    @PutMapping("/{id}")
    @PostMapping("/{id}/update")
    public String update(@PathVariable Long id, @ModelAttribute @Validated UserCreateEditDto user) {
        return userService.update(id, user)
                .map(userReadDto -> "redirect:/users/" + userReadDto.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

//    @DeleteMapping("/{id}")
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        if(!userService.delete(id)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return "redirect:/users/";
    }
}
