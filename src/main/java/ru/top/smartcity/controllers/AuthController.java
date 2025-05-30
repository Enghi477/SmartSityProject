package ru.top.smartcity.controllers;


import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.top.smartcity.DTOs.AuthRequest;
import ru.top.smartcity.DTOs.RegistrationRequest;
import ru.top.smartcity.entities.User;
import ru.top.smartcity.securities.JwtService;
import ru.top.smartcity.services.UserDetailService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserDetailService userDetailService;
    private final JwtService jwtService;


    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("authRequest", new AuthRequest());
        return "login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute("authRequest") AuthRequest request,
                        HttpServletResponse response,
                        Model model) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );

            User user = (User) authentication.getPrincipal();
            String jwt = jwtService.generationToken(user);

            Cookie jwtCookie = new Cookie("jwt", jwt);
            jwtCookie.setHttpOnly(true);
            jwtCookie.setPath("/");
            response.addCookie(jwtCookie);

            return "redirect:/main";
        } catch (Exception e) {
            model.addAttribute("error", "Invalid username or password");
            return "login";
        }
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("registrationRequest", new RegistrationRequest());
        return "register";
    }

    @PostMapping("/register")
    public String register(
            @Valid @ModelAttribute("registrationRequest") RegistrationRequest request,
            BindingResult bindingResult,
            Model model) {

        System.out.println("Received age: " + request.getAge());

        if (bindingResult.hasErrors()) {
            bindingResult.getFieldErrors().forEach(error -> {
                System.out.println("Validation error: " + error.getField() + " - " + error.getDefaultMessage());
            });
            return "register";
        }

        try {
            userDetailService.registerUser(request);
            return "redirect:/auth/login";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "register";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpServletResponse response) {
        Cookie jwtCookie = new Cookie("jwt", null);
        jwtCookie.setHttpOnly(true);
        jwtCookie.setPath("/");
        jwtCookie.setMaxAge(0);
        response.addCookie(jwtCookie);

        return "redirect:/auth/login";
    }
}