package ru.top.smartcity.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.top.smartcity.entities.Landmark;
import ru.top.smartcity.repositories.LandmarkRepository;

@Controller
@RequestMapping("/admin/landmarks")
@PreAuthorize("hasRole('ROLE_ADMIN')")
@RequiredArgsConstructor
public class AdminLandmarkController {
    private final LandmarkRepository landmarkRepository;


    @PostMapping("/add")
    public String addLandmark(@ModelAttribute Landmark landmark,
                              RedirectAttributes redirectAttributes) {
        try {
            landmarkRepository.save(landmark);
            redirectAttributes.addFlashAttribute("successMessage", "Достопримечательность успешно добавлена");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Ошибка при добавлении");
        }
        return "redirect:/landmarks";
    }

    @DeleteMapping("/delete/{id}")
    @ResponseBody
    public ResponseEntity<?> deleteLandmark(@PathVariable Long id) {
        try {
            landmarkRepository.deleteById(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
