package ru.top.smartcity.controllers;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.top.smartcity.DTOs.UserDTO;
import ru.top.smartcity.DTOs.UserFilterDTO;
import ru.top.smartcity.services.UserService;


@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private static final int DEFAULT_PAGE_SIZE = 10;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/dashboard")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String showUserDashboard(
            @Valid @ModelAttribute("filter") UserFilterDTO filter,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "id") String sortField,
            @RequestParam(defaultValue = "asc") String sortDirection,
            Model model) {

        Sort sort = Sort.by(sortField);
        sort = sortDirection.equalsIgnoreCase("desc") ? sort.descending() : sort.ascending();

        Pageable pageable = PageRequest.of(page, DEFAULT_PAGE_SIZE, sort);
        Page<UserDTO> usersPage = userService.getUsers(filter, pageable);

        model.addAttribute("users", usersPage.getContent());
        model.addAttribute("currentPage", usersPage.getNumber());
        model.addAttribute("totalPages", usersPage.getTotalPages());
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDirection", sortDirection);
        model.addAttribute("filter", filter);

        return "admin/dashboard";
    }


    @DeleteMapping("/users/{id}")
    @ResponseBody
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok().build();
    }
}

