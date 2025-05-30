package ru.top.smartcity.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.top.smartcity.services.CategoryService;
import ru.top.smartcity.services.LandmarkService;

import java.util.ArrayList;
import java.util.List;


@Controller
@RequestMapping("/landmarks")
@RequiredArgsConstructor
public class LandmarkController {
    private final LandmarkService landmarkService;
    private final CategoryService categoryService;

    @GetMapping
    public String showLandmarks(
            @RequestParam(required = false, defaultValue = "all") String category,
            Model model, Authentication authentication) {

        model.addAttribute("landmarks", landmarkService.getByCategory(category));
        List<String> allCategories = new ArrayList<>();
        allCategories.addAll(categoryService.getCityCategories());
        allCategories.addAll(categoryService.getSurroundingCategories());
        model.addAttribute("categories", allCategories);
        model.addAttribute("selectedCategory", category);
        boolean isAdmin = false;
        if (authentication != null && authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            isAdmin = true;
        }

        model.addAttribute("isAdmin", isAdmin);
        return "landmarks";
    }
}