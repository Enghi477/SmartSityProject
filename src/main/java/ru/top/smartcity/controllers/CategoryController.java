package ru.top.smartcity.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.top.smartcity.models.Category;
import ru.top.smartcity.services.CategoryService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;


    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String showCategories(Model model) {
        model.addAttribute("cityCategories", categoryService.getCityCategories());
        model.addAttribute("surroundingCategories", categoryService.getSurroundingCategories());
        model.addAttribute("newCategory", new Category());
        return "admin/categories";
    }

    @PostMapping("/update")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String updateCategories(
            @RequestParam("cityCategories") List<String> cityCategories,
            @RequestParam("surroundingCategories") List<String> surroundingCategories,
            Model model) {

        try {
            List<Category> allCategories = new ArrayList<>();

            cityCategories.forEach(name -> {
                if (!name.trim().isEmpty()) {
                    allCategories.add(new Category(name.trim(), "city"));
                }
            });

            surroundingCategories.forEach(name -> {
                if (!name.trim().isEmpty()) {
                    allCategories.add(new Category(name.trim(), "surrounding"));
                }
            });

            categoryService.updateCategories(allCategories);
            model.addAttribute("successMessage", "Категории успешно обновлены!");
        } catch (IOException e) {
            model.addAttribute("errorMessage", "Ошибка при сохранении категорий: " + e.getMessage());
        }

        return showCategories(model);
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String addCategory(@ModelAttribute Category newCategory, Model model) {
        try {
            List<Category> currentCategories = categoryService.getAllCategories();
            currentCategories.add(newCategory);
            categoryService.updateCategories(currentCategories);
            model.addAttribute("successMessage", "Категория добавлена успешно!");
        } catch (IOException e) {
            model.addAttribute("errorMessage", "Ошибка при добавлении категории: " + e.getMessage());
        }

        return showCategories(model);
    }

    @PostMapping("/delete")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String deleteCategory(
            @RequestParam String name,
            @RequestParam String type,
            Model model) {

        try {
            List<Category> currentCategories = categoryService.getAllCategories();
            List<Category> updatedCategories = currentCategories.stream()
                    .filter(c -> !(c.getName().equals(name) && c.getType().equals(type)))
                    .collect(Collectors.toList());

            categoryService.updateCategories(updatedCategories);
            model.addAttribute("successMessage", "Категория удалена успешно!");
        } catch (IOException e) {
            model.addAttribute("errorMessage", "Ошибка при удалении категории: " + e.getMessage());
        }

        return showCategories(model);
    }
}
