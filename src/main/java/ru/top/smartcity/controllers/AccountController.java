package ru.top.smartcity.controllers;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.top.smartcity.DTOs.PasswordChangeDTO;
import ru.top.smartcity.DTOs.UserDTO;
import ru.top.smartcity.DTOs.UserUpdateDTO;
import ru.top.smartcity.securities.JwtService;
import ru.top.smartcity.services.UserServiceImp;

import java.security.Principal;


@Controller
@RequestMapping("/account")
@RequiredArgsConstructor
public class AccountController {
    private final UserServiceImp userService;
    private final JwtService jwtService;
    private final HttpServletResponse response;

    @GetMapping
    public String accountPage(Principal principal, Model model) {
        UserDTO user = userService.getCurrentUser(principal.getName());
        model.addAttribute("user", user);

        UserUpdateDTO updateDTO = new UserUpdateDTO();
        updateDTO.setFirstname(user.getFirstname());
        updateDTO.setLastname(user.getLastname());
        updateDTO.setAge(user.getAge());
        updateDTO.setPhone(user.getPhone());
        updateDTO.setEmail(user.getEmail());

        model.addAttribute("updateDTO", updateDTO);
        model.addAttribute("passwordChangeDTO", new PasswordChangeDTO());
        return "account";
    }

    @PostMapping("/update")
    public String updateProfile(
            @Valid @ModelAttribute("updateDTO") UserUpdateDTO updateDTO,
            BindingResult bindingResult,
            Principal principal,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            return "account";
        }

        try {
            UserDTO userDTO = userService.getCurrentUser(principal.getName());
            userService.updateUserProfile(userDTO.getId(), updateDTO);

            userService.refreshAuthentication(updateDTO.getEmail());

            UserDetails userDetails = userService.loadUserByUsername(updateDTO.getEmail());
            String newToken = jwtService.generationToken(userDetails);

            Cookie jwtCookie = new Cookie("jwt", newToken);
            jwtCookie.setHttpOnly(true);
            jwtCookie.setPath("/");
            response.addCookie(jwtCookie);

            redirectAttributes.addFlashAttribute("success", "Профиль успешно обновлен");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка при обновлении профиля: " + e.getMessage());
        }
        return "redirect:/account";
    }


    @PostMapping("/change-password")
    public String changePassword(@Valid @ModelAttribute("passwordChangeDTO") PasswordChangeDTO passwordChangeDTO,
                                 BindingResult bindingResult,
                                 Principal principal,
                                 HttpServletRequest request,
                                 RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "account";
        }

        try {
            UserDTO userDTO = userService.getCurrentUser(principal.getName());
            userService.changePassword(userDTO.getId(), passwordChangeDTO);
            userService.refreshAuthentication(principal.getName());

            UserDetails userDetails = userService.loadUserByUsername(principal.getName());
            String newToken = jwtService.generationToken(userDetails);

            Cookie jwtCookie = new Cookie("jwt", newToken);
            jwtCookie.setHttpOnly(true);
            jwtCookie.setPath("/");
            response.addCookie(jwtCookie);

            redirectAttributes.addFlashAttribute("success", "Пароль успешно изменён");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/account";
    }
}
