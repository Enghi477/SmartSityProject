package ru.top.smartcity.utils;


import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import ru.top.smartcity.repositories.LandmarkRepository;

import java.io.FileNotFoundException;

@Component
public class DataInitializerLandmark {
    private static final Logger log = LoggerFactory.getLogger(DataInitializerLandmark.class);

    private final LandmarkRepository landmarkRepository;
    private final ResourceLoader resourceLoader;

    public DataInitializerLandmark(LandmarkRepository landmarkRepository,
                                   ResourceLoader resourceLoader) {
        this.landmarkRepository = landmarkRepository;
        this.resourceLoader = resourceLoader;
    }

    @PostConstruct
    @Transactional
    public void init() {
        try {
            if (landmarkRepository.count() > 0) {
                log.info("Landmarks data already initialized");
                return;
            }

            Resource resource = resourceLoader.getResource("classpath:data/data.json");
            if (!resource.exists()) {
                throw new FileNotFoundException("Data file not found");
            }

            ObjectMapper mapper = new ObjectMapper();
            CombinedData data = mapper.readValue(resource.getInputStream(), CombinedData.class);

            if (data.getLandmarks() == null || data.getLandmarks().isEmpty()) {
                log.warn("No landmarks found in data file");
                return;
            }

            landmarkRepository.saveAll(data.getLandmarks());
            log.info("Successfully initialized {} landmarks", data.getLandmarks().size());
        } catch (Exception e) {
            log.error("Failed to initialize landmarks data", e);
            throw new RuntimeException("Landmarks data initialization failed", e);
        }
    }
}