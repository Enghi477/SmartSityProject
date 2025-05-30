package ru.top.smartcity.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.top.smartcity.entities.User;
import ru.top.smartcity.repositories.UserRepository;
import ru.top.smartcity.services.CategoryService;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final UserRepository userRepository;
    private final CategoryService categoryService;


    @GetMapping("/")
    public String home() {
        return "home";
    }


    @GetMapping("/main")
    public String mainPage(Model model, Principal principal) {
        User user = userRepository.findByEmailWithRoles(principal.getName())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        model.addAttribute("firstname", user.getFirstname());
        model.addAttribute("cityCategories", categoryService.getCityCategories());
        model.addAttribute("surroundingCategories", categoryService.getSurroundingCategories());
        return "main";
    }

    @GetMapping("/error")
    public String error() {
        return "error";
    }

    @GetMapping("/history")
    public String historyPage() {
        return "history";
    }

    @GetMapping("/info")
    public String infoPage() {
        return "info";
    }

    @GetMapping("/statistic")
    public String statisticPage() {
        return "statistic";
    }
}
