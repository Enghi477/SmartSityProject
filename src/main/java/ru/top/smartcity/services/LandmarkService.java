package ru.top.smartcity.services;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.top.smartcity.entities.Landmark;
import ru.top.smartcity.repositories.LandmarkRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LandmarkService {
    private final LandmarkRepository landmarkRepository;

    public List<Landmark> getAllLandmarks() {
        return landmarkRepository.findAll();
    }

    public List<Landmark> getByCategory(String category) {
        return "all".equals(category) ?
                getAllLandmarks() :
                landmarkRepository.findByCategory(category);
    }

    public List<String> getAllCategories() {
        return landmarkRepository.findDistinctCategories();
    }

    public Landmark getById(Long id) {
        return landmarkRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Landmark not found"));
    }
}
